package com.example.swiftwipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class checkout extends AppCompatActivity {
    private static final String BACKEND_URL = "https://stripe-payment-swiftswipe.herokuapp.com/";
    private final OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    private double finalCost;
    private String idOfTransaction, finalValueString, cost;
    private FirebaseAuth fAuth;
    private DatabaseReference toPath, fromPath;
    private boolean couponUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        // getting the total cost of the cart passed over from the cart activity
        cost = getIntent().getStringExtra("EXTRA_SESSION_ID");
        couponUsed = getIntent().getBooleanExtra("coupon", false);
        // setting the amount the user will be paying when they click pay
        finalCost = Double.parseDouble(cost);
        DecimalFormat df = new DecimalFormat("##.##");
        finalValueString = df.format(finalCost);
        finalCost = Double.parseDouble(finalValueString);
        // don't steal my key please & thank you
        stripe = new Stripe(
                getApplicationContext(),
                "pk_test_51IfNeFIcNwtJp8IXhYhG4RxT7hdZbi6uzeCEQL4Gz7SPOWrMPdiQjViUL3EJ7MoErms6sWfApkBnj1IXQaTpPXTR00rcfN2LBg"
        );
        // getting the employees unique log in number
        fAuth = FirebaseAuth.getInstance();
        String authUid = fAuth.getUid();
        // getting the date and time the order was made
        String myCurrentDateTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        // getting the path from where the objects in the cart are stored
        fromPath = FirebaseDatabase.getInstance().getReference("User").child(authUid).child("cart");
        // setting the path to where I want the objects in cart to go to on firebase
        // this is done so that there is an order history on the database
        toPath = FirebaseDatabase.getInstance().getReference("User").child(authUid).child("order").child(myCurrentDateTime);
        startCheckout();
    }

    private void startCheckout() {
        // Create a PaymentIntent by calling the server's endpoint.
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        // Could be improved that I deal with ints from beginning but codebase is delicate
        // and submission is due soon
        int amount = (int) (finalCost * 100);
        Map<String,Object> payMap = new HashMap<>();
        Map<String,Object> itemMap = new HashMap<>();
        List<Map<String, Object>> itemList = new ArrayList<>();
        payMap.put("currency","eur");
        itemMap.put("id", "photo_subscription");
        itemMap.put("amount",amount);
        itemList.add(itemMap);
        payMap.put("items",itemList);
        String json = new Gson().toJson(payMap);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "create-payment-intent")
                .post(body)
                .build();
        httpClient.newCall(request)
                .enqueue(new PayCallback(this));
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(this, confirmParams);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }
    private final class PaymentResultCallback implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<checkout> activityRef;
        PaymentResultCallback(@NonNull checkout activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final checkout activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                // getting the id of the transaction for doing returns
                try {
                    JSONObject obj = new JSONObject(gson.toJson(paymentIntent));
                    idOfTransaction = obj.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // for each object in the cart (cart branch) push to the order branch
                fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        toPath.setValue(snapshot.getValue());
                        orderInfo();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                // if this sleep isn't done the cart branch will get deleted before the objects
                // can be moved over to the order branch
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // emptying the cart after the order has been successful
                fromPath.removeValue();
                // informing the employee the order was a success
                Toast.makeText(activity, "Order completed!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }
        @Override
        public void onError(@NonNull Exception e) {
            final checkout activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }

    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }

    private static final class PayCallback implements Callback {
        @NonNull private final WeakReference<checkout> activityRef;
        PayCallback(@NonNull checkout activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final checkout activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(activity, "Error: " + e.toString(), Toast.LENGTH_LONG).show()
            );
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final checkout activity = activityRef.get();
            if (activity == null) {
                return;
            }
            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG).show()
                );
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }

    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");
    }

    public void orderInfo () {
        orderInfo orderInfo = new orderInfo(idOfTransaction, finalValueString, false, couponUsed);
        toPath.child("orderInfo").setValue(orderInfo);
    }
}
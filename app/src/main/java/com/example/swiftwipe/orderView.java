package com.example.swiftwipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.gson.Gson;
import com.stripe.android.Stripe;
import com.stripe.android.model.*;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class orderView extends AppCompatActivity {
    DatabaseReference userdbref;
    RecyclerView recyclerView;
    private OkHttpClient httpClient = new OkHttpClient();
    FirebaseAuth fAuth;
    InformationAdapter adapter;
    private static final String BACKEND_URL = "https://stripe-payment-swiftswipe.herokuapp.com/";
    private Stripe stripe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);
        recyclerView = findViewById(R.id.recyclerView2);

        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51IfNeFIcNwtJp8IXhYhG4RxT7hdZbi6uzeCEQL4Gz7SPOWrMPdiQjViUL3EJ7MoErms6sWfApkBnj1IXQaTpPXTR00rcfN2LBg")
        );

        String branchId = getIntent().getStringExtra("branch");
        System.out.println(branchId);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        fAuth = FirebaseAuth.getInstance();
        String authUid = fAuth.getUid();

        // branch where the objects I want are
        userdbref = FirebaseDatabase.getInstance().getReference("User").child(authUid).child("order").child(branchId);

        // querying that branch providing my Information class
        // this query should return back an array of information objects depending on how many it can get from firebase
        FirebaseRecyclerOptions<Information> options = new FirebaseRecyclerOptions.Builder<Information>().setQuery(userdbref, Information.class).build();

        adapter = new InformationAdapter(options);
        recyclerView.setAdapter(adapter);
    }

    // these two methods(onStart & onStop) are absolutely necessary for the recyclerview to populate
    // firebase adapters have to listen I guess
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    // on click method for the refund button in activity_order_view
    public void refund(View view) {
        // creating my request to send to my Node.js server
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        Map<String,Object> payMap = new HashMap<>();
        payMap.put("currency","eur");
        String json = new Gson().toJson(payMap);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder().url(BACKEND_URL + "create-payment-refund").post(body).build();
        // sending the request
        httpClient.newCall(request).enqueue(new orderView.PayCallback(this));

    }

    private static final class PayCallback implements Callback {
        @NonNull private final WeakReference<orderView> activityRef;
        PayCallback(@NonNull orderView activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final orderView activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(activity, "Error: " + e.toString(), Toast.LENGTH_LONG).show()
            );
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {

        }
    }
}
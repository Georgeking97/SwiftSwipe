package com.example.swiftwipe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class orderView extends AppCompatActivity {
    private static final String BACKEND_URL = "https://stripe-payment-swiftswipe.herokuapp.com/";
    private final OkHttpClient httpClient = new OkHttpClient();
    private FirebaseAuth fAuth;
    private FirebaseRecyclerOptions<Information> options;
    private DatabaseReference userdbref;
    private RecyclerView recyclerView;
    private InformationAdapter adapter;
    private TextView transactionIdTxt, priceTxt, couponTxt, returnedTxt;
    private String id;
    private int totalObjects;
    private Button refundButton;
    private Boolean returned;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);

        recyclerView = findViewById(R.id.recyclerView2);
        transactionIdTxt = findViewById(R.id.transactionIdTxt);
        priceTxt = findViewById(R.id.priceTxt);
        couponTxt = findViewById(R.id.couponTxt);
        returnedTxt = findViewById(R.id.returnedTxt);
        refundButton = findViewById(R.id.refundButton);
            
        String branchId = getIntent().getStringExtra("branch");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        fAuth = FirebaseAuth.getInstance();
        String authUid = fAuth.getUid();

        userdbref = FirebaseDatabase.getInstance().getReference("User").child(authUid).child("order").child(branchId);

        settingRecycler();
        populatingOrderInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void populatingOrderInfo(){
        userdbref.child("orderInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderInfo orderInfo = snapshot.getValue(orderInfo.class);
                id = orderInfo.getTransactionId();
                returned = orderInfo.isReturned();
                transactionIdTxt.setText(id);
                priceTxt.setText(orderInfo.getCost());
                couponTxt.setText(String.valueOf(orderInfo.isCoupon()));
                returnedTxt.setText(String.valueOf(returned));
                // so that an order can't be returned more than once
                if (returned){
                    refundButton.setText("Order has been refunded");
                    refundButton.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void settingRecycler(){
        userdbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalObjects = (int) snapshot.getChildrenCount();
                options = new FirebaseRecyclerOptions.Builder<Information>().setQuery(userdbref.limitToFirst(totalObjects - 1), Information.class).build();
                adapter = new InformationAdapter(options);
                recyclerView.setAdapter(adapter);
                adapter.startListening();
                // if you remove this the program will crash when you click on an item view in the recycler
                adapter.setOnItemClickListener((position, v) -> {
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void refund(View view) {
        // creating my request to send to my Node.js server
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        Map<String, Object> idMap = new HashMap<>();
        // my node.js server is expecting an id identifier and then the id object which is the transaction id
        idMap.put("id", id);
        String json = new Gson().toJson(idMap);
        RequestBody body = RequestBody.create(json, mediaType);
        // build the request to send, create-payment-refund is the endpoint
        Request request = new Request.Builder().url(BACKEND_URL + "create-payment-refund").post(body).build();
        // sending the request
        httpClient.newCall(request).enqueue(new orderView.PayCallback(this));
        // updating the return status on the database
        userdbref.child("orderInfo").child("returned").setValue(true);
        refundButton.setText("Order has been refunded");
        refundButton.setEnabled(false);
        Toast.makeText(orderView.this, "Refund successful.", Toast.LENGTH_SHORT).show();
    }

    private static final class PayCallback implements Callback {
        @NonNull
        private final WeakReference<orderView> activityRef;

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
        public void onResponse(@NonNull Call call, @NonNull final Response response) {

        }
    }
}
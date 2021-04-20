package com.example.swiftwipe;

import android.os.Bundle;
import android.view.View;
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
import com.stripe.android.Stripe;

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
    private static final String BACKEND_URL = "https://stripe-payment-swiftswipe.herokuapp.com/";
    DatabaseReference userdbref;
    RecyclerView recyclerView;
    FirebaseAuth fAuth;
    InformationAdapter adapter;
    int totalObjects;
    FirebaseRecyclerOptions<Information> options;
    private OkHttpClient httpClient = new OkHttpClient();
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
        userdbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalObjects = (int) snapshot.getChildrenCount();
                options = new FirebaseRecyclerOptions.Builder<Information>().setQuery(userdbref.limitToFirst(totalObjects-1), Information.class).build();
                adapter = new InformationAdapter(options);
                recyclerView.setAdapter(adapter);
                adapter.startListening();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // these two methods(onStart & onStop) are absolutely necessary for the recyclerview to populate
    // firebase adapters have to listen I guess
    @Override
    protected void onStart() {
        super.onStart();
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
        Map<String, Object> payMap = new HashMap<>();
        payMap.put("currency", "eur");
        String json = new Gson().toJson(payMap);
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder().url(BACKEND_URL + "create-payment-refund").post(body).build();
        // sending the request
        httpClient.newCall(request).enqueue(new orderView.PayCallback(this));

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
        public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {

        }
    }
}
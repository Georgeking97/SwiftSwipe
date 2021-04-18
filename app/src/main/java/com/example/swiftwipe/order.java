package com.example.swiftwipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class order extends AppCompatActivity {

    private RecyclerView recyclerView;
    FirebaseAuth fAuth;
    DatabaseReference userdbref;
    private orderAdapter adapter;
    private ArrayList<orderModel> list;
    private orderAdapter.clickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        setOnClick();
        adapter = new orderAdapter(list, this, listener);
        recyclerView.setAdapter(adapter);
        // gets the unique token associated with the signed in account from firebase auth
        fAuth = FirebaseAuth.getInstance();
        String authUid = fAuth.getUid();
        userdbref = FirebaseDatabase.getInstance().getReference("User").child(authUid).child("order");
        // this allows me to populate my recyclerview with the date the order was made
        userdbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    // setting up my order object
                    orderModel model = ds.getValue(orderModel.class);
                    // remapping the only variable to the parent key
                    String key =  ds.getKey();
                    model.setKey(key);
                    // increasing the array size for the adapter
                    list.add(model);
                }
                // letting the adapter know that to check the array again as it's increased in size
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setOnClick() {
        listener = new orderAdapter.clickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), orderView.class);
                startActivity(intent);
            }
        };
    }
}
package com.example.swiftwipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class orderView extends AppCompatActivity {
    DatabaseReference userdbref;
    RecyclerView recyclerView;
    FirebaseAuth fAuth;
    InformationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);
        recyclerView = findViewById(R.id.recyclerView2);

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
    // these two methods are absolutely necessary for the recyclerview to populate
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
}
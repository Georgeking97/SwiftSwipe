package com.example.swiftwipe;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Search extends AppCompatActivity {
    DatabaseReference dbref;
    RecyclerView recyclerView;
    InformationAdapter adapter;
    EditText searchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.result_list);
        searchInput = findViewById(R.id.searchBar);

        // providing path to firebase branch I want to query
        dbref = FirebaseDatabase.getInstance().getReference("Test");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // db query to populate the recyclerview
        FirebaseRecyclerOptions<Information> options = new FirebaseRecyclerOptions.Builder<Information>().setQuery(dbref, Information.class).build();
        adapter = new InformationAdapter(options);
        recyclerView.setAdapter(adapter);

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    System.out.println(ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    FirebaseRecyclerOptions<Information> options = new FirebaseRecyclerOptions.Builder<Information>().setQuery(dbref, Information.class).build();
                    adapter.updateOptions(options);
                } else {
                    FirebaseRecyclerOptions<Information> options = new FirebaseRecyclerOptions.Builder<Information>().setQuery(dbref.orderByChild("productName").equalTo(s.toString().toLowerCase()), Information.class).build();
                    adapter.updateOptions(options);
                }
            }
        });

        adapter.setOnItemClickListener(new InformationAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent i = new Intent(getApplicationContext(), product.class);
                TextView idView = v.findViewById(R.id.id);
                String positionStr = idView.getText().toString();
                System.out.println("Position String: "+positionStr);
                i.putExtra("EXTRA_SESSION_ID", positionStr);
                startActivity(i);
            }
        });
    }

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

    //starting up the home activity
    public void home(View view) {
        Search.this.finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    //starting up the search function
    public void search(View view) {
        Search.this.finish();
        startActivity(new Intent(getApplicationContext(), Search.class));
    }

    //Starting the barcode scanner
    public void scan(View view) {
        Search.this.finish();
        startActivity(new Intent(getApplicationContext(), Scanner.class));
    }

    //starting the cart activity
    public void cart(View view) {
        Search.this.finish();
        startActivity(new Intent(getApplicationContext(), Cart.class));
    }
}
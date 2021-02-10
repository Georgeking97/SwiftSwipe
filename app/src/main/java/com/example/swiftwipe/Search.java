package com.example.swiftwipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Search extends AppCompatActivity {
    DatabaseReference dbref;
    RecyclerView recyclerView;
    InformationAdapter adapter;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbref = FirebaseDatabase.getInstance().getReference("Test");
        recyclerView = findViewById(R.id.result_list);
        search = findViewById(R.id.search);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        FirebaseRecyclerOptions<Information> options = new FirebaseRecyclerOptions.Builder<Information>().setQuery(dbref, Information.class).build();
        adapter = new InformationAdapter(options);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new InformationAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent i = new Intent(getApplicationContext(), product.class);
                TextView idView = v.findViewById(R.id.id);
                String positionStr = idView.getText().toString();
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

    //takes the users input for the search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView=(SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    //creates a query of the firebase database and returns all relevant results and sets it to the recyclerview
    private void search(String query) {
        FirebaseRecyclerOptions<Information> options = new FirebaseRecyclerOptions.Builder<Information>().setQuery(FirebaseDatabase.getInstance().getReference().child("Test").orderByChild("productName").startAt(query).endAt(query+" \uf8ff"), Information.class).build();
        adapter = new InformationAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}
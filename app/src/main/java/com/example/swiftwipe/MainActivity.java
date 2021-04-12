package com.example.swiftwipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //logging out and starting the login activity
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        MainActivity.this.finish();
        startActivity(new Intent(MainActivity.this, Login.class));
    }

    //starting up the search function
    public void search(View view) {
        MainActivity.this.finish();
        startActivity(new Intent(MainActivity.this, Search.class));
    }

    //Starting the barcode scanner
    public void scan(View view) {
        MainActivity.this.finish();
        startActivity(new Intent(getApplicationContext(), Scanner.class));
    }
    //starting the cart activity
    public void cart(View view) {
        MainActivity.this.finish();
        startActivity(new Intent(getApplicationContext(), Cart.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
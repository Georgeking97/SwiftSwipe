package com.example.swiftwipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button ScannerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScannerBtn = findViewById(R.id.scannerBtn);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
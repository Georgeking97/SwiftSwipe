package com.example.swiftwipe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button ScannerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScannerBtn = findViewById(R.id.scannerBtn);
    }

    public void logout(View view) {
        //logging out and starting the login activity
        FirebaseAuth.getInstance().signOut();
        MainActivity.this.finish();
        startActivity(new Intent(MainActivity.this, Login.class));
    }

    public void scan(View view) {
        MainActivity.this.finish();
        startActivity(new Intent(getApplicationContext(), Scanner.class));
    }

}
package com.example.swiftwipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class product extends AppCompatActivity {
    Information newInformation;
    TextView name, price, size;
    ImageView image;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        String productId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbref = database.getReference("Test").child(productId);

        name = (TextView) findViewById(R.id.productName);
        price = (TextView) findViewById(R.id.productPriceTxt);
        size = (TextView) findViewById(R.id.productSizeTxt);
        image = (ImageView) findViewById(R.id.productImage);

        addValueEventListener();
    }

    private void addValueEventListener() {
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newInformation = snapshot.getValue(Information.class);
                name.setText(newInformation.getProductName());
                price.setText(newInformation.getProductPrice()+"");
                size.setText(newInformation.getProductSize());
                Glide.with(image.getContext()).load(newInformation.getProductImage()).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
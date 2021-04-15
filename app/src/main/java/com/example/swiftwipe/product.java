package com.example.swiftwipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class product extends AppCompatActivity {
    Information newInformation;
    TextView name, price, size, id;
    ImageView image;
    Button button;
    DatabaseReference dbref, userdbref;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        name = (TextView) findViewById(R.id.productName);
        price = (TextView) findViewById(R.id.productPriceTxt);
        size = (TextView) findViewById(R.id.productSizeTxt);
        image = (ImageView) findViewById(R.id.productImage);
        button = (Button) findViewById(R.id.addBtn);
        id = (TextView) findViewById(R.id.id);

        // getting the id from the search activity, required to allow me to populate the xml view
        String productId = getIntent().getStringExtra("EXTRA_SESSION_ID");

        // setting up the path to the product on the firebase database, getting the data for the xml view
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbref = database.getReference("Test").child(productId);

        // setting up the path to the cart branch to push the object value to
        fAuth = FirebaseAuth.getInstance();
        String authUid = fAuth.getUid();
        userdbref = FirebaseDatabase.getInstance().getReference("User").child(authUid).child("cart");

        setValues();
        addItemToCart();
    }

    // setting the values in the xml view based on the product ID passed in from search
    private void setValues() {
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newInformation = snapshot.getValue(Information.class);
                name.setText(newInformation.getProductName());
                price.setText(newInformation.getProductPrice()+"");
                size.setText(newInformation.getProductSize());
                id.setText(newInformation.getProductid());
                Glide.with(image.getContext()).load(newInformation.getProductImage()).into(image);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // adding the product retrieved from the product branch to the cart branch
    private void addItemToCart(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        fAuth = FirebaseAuth.getInstance();
                        String authUid = fAuth.getUid();
                        newInformation = snapshot.getValue(Information.class);
                        String key = userdbref.push().getKey();
                        newInformation.setProductid(key);
                        userdbref = FirebaseDatabase.getInstance().getReference("User").child(authUid).child("cart").child(key);
                        userdbref.setValue(newInformation);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

                //switching over to the cart activity
                Intent intent = new Intent(getApplicationContext(), Cart.class);
                startActivity(intent);
            }
        });
    }

    //starting up the home activity
    public void home(View view) {
        product.this.finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    //starting up the search function
    public void search(View view) {
        product.this.finish();
        startActivity(new Intent(getApplicationContext(), Search.class));
    }

    //Starting the barcode scanner
    public void scan(View view) {
        product.this.finish();
        startActivity(new Intent(getApplicationContext(), Scanner.class));
    }
    //starting the cart activity
    public void cart(View view) {
        product.this.finish();
        startActivity(new Intent(getApplicationContext(), Cart.class));
    }
}
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class product extends AppCompatActivity {
    Information newInformation;
    TextView name, price, size, id2;
    ImageView image;
    Button button;
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        // getting the id from the search activity
        String productId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        // setting up the path to the product on the firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbref = database.getReference("Test").child(productId);

        name = (TextView) findViewById(R.id.productName);
        price = (TextView) findViewById(R.id.productPriceTxt);
        size = (TextView) findViewById(R.id.productSizeTxt);
        image = (ImageView) findViewById(R.id.productImage);
        button = (Button) findViewById(R.id.addBtn);
        id2 = (TextView) findViewById(R.id.id2);

        //calling the method that sets the values in the xml view
        addValueEventListener();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting the product id to send to the cart class, why do I do this?
                TextView textview1 = (TextView) findViewById(R.id.id2);
                String text = textview1.getText().toString();
                //switching over to the cart activity
                Intent intent = new Intent(getApplicationContext(), Cart.class);
                intent.putExtra("EXTRA_SESSION_ID", text);
                startActivity(intent);
            }
        });
    }

    // setting the values in the xml view based on the product ID passed in from search
    private void addValueEventListener() {
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newInformation = snapshot.getValue(Information.class);
                name.setText(newInformation.getProductName());
                price.setText(newInformation.getProductPrice()+"");
                size.setText(newInformation.getProductSize());
                id2.setText(newInformation.getProductid());
                Glide.with(image.getContext()).load(newInformation.getProductImage()).into(image);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
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
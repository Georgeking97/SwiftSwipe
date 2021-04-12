package com.example.swiftwipe;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Cart extends AppCompatActivity {
    DatabaseReference dbref;
    DatabaseReference userdbref;
    RecyclerView recyclerView;
    FirebaseAuth fAuth;
    CartAdapter adapter;
    Information newInformation;

    TextView total;
    Button checkout, applyCouponBtn;
    EditText coupon;

    int finalvalue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        checkout = findViewById(R.id.payBtn);
        recyclerView = findViewById(R.id.cart_list);
        applyCouponBtn = findViewById(R.id.applyCoupon);
        coupon = findViewById(R.id.couponValue);
        total = findViewById(R.id.totalAmountTxt);

        //gets the id of the product passed in from the product class, this is used to get the entire object from firebase (getting the entire object is done in the addValueEventListener class)
        String productId = getIntent().getStringExtra("EXTRA_SESSION_ID");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //if statement to ensure that if the cart activity is opened from a path that doesn't provide an ID that the application doesn't crash
        if (productId != null) {
            //returns the item we added from the product class
            dbref = database.getReference("Test").child(productId);
            //pushing the product to the users individual branch
            addValueEventListener();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        //gets the unique token associated with the signed in account from firebase auth
        fAuth = FirebaseAuth.getInstance();
        String authUid = fAuth.getUid();
        //points to the users branch on the database based upon their unique token
        userdbref = FirebaseDatabase.getInstance().getReference("Test2").child(authUid);
        //queries the users branch for all products and populates the recyclerview with them
        FirebaseRecyclerOptions<Information> options = new FirebaseRecyclerOptions.Builder<Information>().setQuery(userdbref, Information.class).build();
        //passes the results over to the cart adapter to populate the single view items (cartlist.xml)
        adapter = new CartAdapter(options);
        recyclerView.setAdapter(adapter);

        //getting the total cost of the entire cart
        userdbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for loop that looks at each object on the path provided (userdbref)
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Information info = ds.getValue(Information.class);
                    //adding the individual prices together to calculate the total
                    finalvalue = finalvalue + info.getProductPrice();
                    //setting the total to the textview
                    total.setText(finalvalue+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // currently trying to apply the coupon discount
        applyCouponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting the coupon code the user entered
                String input = coupon.getText().toString().trim();
                // error handling if no coupon has been entered and they click the button
                if (TextUtils.isEmpty(input)) {
                    coupon.setError("text is Required");
                    return;
                } else {
                    //query the databases coupon tree
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Coupon");
                    db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot snapshot5 : snapshot.getChildren()){
                                System.out.println("value: "+snapshot5.getValue());
                                if(input.equals(snapshot5.getValue())){
                                    System.out.println("hey");
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), checkout.class);
                String pass = finalvalue + "";
                intent.putExtra("EXTRA_SESSION_ID", pass);
                startActivity(intent);
            }
        });
    }

    private void addValueEventListener() {
        //takes a snapshot of the object in the database so that we can use it's attributes to fill out the recyclerview
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //gets the attributes from the snapshot (name, size, price etc..)
                newInformation = snapshot.getValue(Information.class);
                //pushes the object to the users individual branch so that their cart isn't lost when going to a different activity or adding a new item
                userdbref.push().setValue(newInformation);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        Cart.this.finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    //starting up the search function
    public void search(View view) {
        Cart.this.finish();
        startActivity(new Intent(getApplicationContext(), Search.class));
    }

    //Starting the barcode scanner
    public void scan(View view) {
        Cart.this.finish();
        startActivity(new Intent(getApplicationContext(), Scanner.class));
    }

    //starting the cart activity
    public void cart(View view) {
        Cart.this.finish();
        startActivity(new Intent(getApplicationContext(), Cart.class));
    }
}
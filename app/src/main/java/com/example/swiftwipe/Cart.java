package com.example.swiftwipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Cart extends AppCompatActivity {
    private DatabaseReference userdbref;
    private RecyclerView recyclerView;
    private FirebaseAuth fAuth;
    private CartAdapter adapter;
    private TextView total;
    private Button checkout, applyCouponBtn;
    private EditText coupon;
    private boolean example = false;
    private double finalValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        checkout = findViewById(R.id.payBtn);
        recyclerView = findViewById(R.id.cart_list);
        applyCouponBtn = findViewById(R.id.applyCoupon);
        coupon = findViewById(R.id.couponValue);
        total = findViewById(R.id.totalAmountTxt);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // gets the unique token associated with the signed in account from firebase auth
        fAuth = FirebaseAuth.getInstance();
        String authUid = fAuth.getUid();

        // points to the users branch on the database based upon their unique token
        userdbref = FirebaseDatabase.getInstance().getReference("User").child(authUid).child("cart");

        // queries the users branch for all products and populates the recyclerview with them
        FirebaseRecyclerOptions<Information> options = new FirebaseRecyclerOptions.Builder<Information>().setQuery(userdbref, Information.class).build();

        // passes the results over to the cart adapter to populate the single view items (cartlist.xml)
        adapter = new CartAdapter(options);
        recyclerView.setAdapter(adapter);

        applyCoupon();
        totalCost();
        openCheckout();
    }

    private void totalCost(){
        userdbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for loop that looks at each object on the path provided (userdbref)
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Information info = ds.getValue(Information.class);
                    //adding the individual prices together to calculate the total
                    finalValue = finalValue + info.getProductPrice();
                    //setting the total to the textview
                    total.setText(finalValue+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    // gets the unique token associated with the signed in account from firebase auth
    private void applyCoupon(){
        Context context = getApplicationContext();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Coupon").child("Coupons");

        applyCouponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting the coupon code the user entered
                String input = coupon.getText().toString().trim();
                // error handling if no coupon has been entered and they click the button
                if (TextUtils.isEmpty(input)) {
                    coupon.setError("Please enter a coupon");
                    return;
                } else {
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()){
                                Coupon coupon = ds.getValue(Coupon.class);
                                //if the users entered value matches a coupon on the database and the employee hasn't already used a coupon
                                if (coupon.getCode().equals(input) && example == false){
                                    // dividing the total basket cost by 100 and multiplying it by the discount of the code to get the amount to take away
                                    double percentage = finalValue/100 * coupon.getValue();
                                    // taking the amount away from the total cost and updating the view
                                    finalValue = finalValue - percentage;
                                    // letting the user know the coupon was successfully applied
                                    Toast.makeText(context, "Coupon applied!", Toast.LENGTH_SHORT).show();
                                    total.setText(finalValue+"");
                                    // if a coupon is used we set the used coupon branch to true to only allow one coupon used per cart
                                    //test[0] = true;
                                    example = true;
                                    break;
                                } else {
                                    Toast.makeText(context, "Coupon doesn't exist!", Toast.LENGTH_SHORT).show();
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
    }

    private void openCheckout(){
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), checkout.class);
                String pass = finalValue + "";
                intent.putExtra("EXTRA_SESSION_ID", pass);
                intent.putExtra("coupon", example);
                startActivity(intent);
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
package com.example.swiftwipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Cart extends AppCompatActivity {
    private DatabaseReference userdbref;
    private RecyclerView recyclerView;
    private FirebaseAuth fAuth;
    private CartAdapter adapter;
    private TextView total;
    private Button checkout, applyCouponBtn, clearSale;
    private EditText coupon;
    private String finalValueString;
    private int size;
    private boolean couponUsed = false;
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
        clearSale = findViewById(R.id.clearSale);

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
        clearCart();
    }

    private void clearCart() {
        clearSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdbref.setValue(null);
            }
        });
    }

    public void totalCost() {
        userdbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for loop that looks at each object on the path provided (userdbref)
                for (DataSnapshot ds : snapshot.getChildren()) {
                    size+= ds.getChildrenCount();
                    Information info = ds.getValue(Information.class);
                    //adding the individual prices together to calculate the total
                    finalValue = finalValue + info.getProductPrice();
                    //setting the total to the textview
                    DecimalFormat df = new DecimalFormat("##.##");
                    finalValueString = df.format(finalValue);
                }
                if (finalValueString == null){
                    total.setText("€0.0");
                }else {
                    total.setText("€" + finalValueString);
                }
                // used to ensure that an item is in the cart before allowing a coupon to be used
                size = size/5;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void applyCoupon() {
        Context context = getApplicationContext();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Coupon").child("Coupons");
        applyCouponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting the coupon code the user entered
                String input = coupon.getText().toString().toLowerCase().trim();
                // error handling if no coupon has been entered and they click the button
                if (TextUtils.isEmpty(input)) {
                    coupon.setError("Please enter a coupon");
                    return;
                } else {
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                Coupon coupon = ds.getValue(Coupon.class);
                                //if the users entered value matches a coupon on the database and the employee hasn't already used a coupon and there is an item in the cart
                                if (coupon.getCode().equals(input) && couponUsed == false && size > 0) {
                                    // dividing the total basket cost by 100 and multiplying it by the discount of the code to get the amount to take away
                                    double percentage = finalValue / 100 * coupon.getValue();
                                    // taking the amount away from the total cost and updating the view
                                    finalValue = finalValue - percentage;
                                    DecimalFormat df = new DecimalFormat("##.##");
                                    String finalValueString = df.format(finalValue);
                                    // letting the user know the coupon was successfully applied
                                    Toast.makeText(context, "Coupon applied!", Toast.LENGTH_SHORT).show();
                                    total.setText("€"+finalValueString);
                                    // if a coupon is used we
                                    couponUsed = true;
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

    private void openCheckout() {
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalValue > 1){
                    Intent intent = new Intent(getApplicationContext(), checkout.class);
                    String pass = finalValue + "";
                    intent.putExtra("EXTRA_SESSION_ID", pass);
                    intent.putExtra("coupon", couponUsed);
                    startActivity(intent);
                } else {
                    Context context = getApplicationContext();
                    Toast.makeText(context, "Please add an item before trying to checkout!", Toast.LENGTH_SHORT).show();
                }
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
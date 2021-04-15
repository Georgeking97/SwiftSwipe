package com.example.swiftwipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class CartAdapter extends FirebaseRecyclerAdapter<Information, CartAdapter.MyViewHolder> {
    FirebaseAuth fAuth;

    public CartAdapter(@NonNull FirebaseRecyclerOptions<Information> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull Information model) {
        fAuth = FirebaseAuth.getInstance();
        String authUid = fAuth.getUid();
        holder.name.setText(model.getProductName());
        holder.size.setText(model.getProductSize());
        holder.price.setText(model.getProductPrice() + "");
        holder.id.setText(model.getProductid());
        Glide.with(holder.image.getContext()).load(model.getProductImage()).into(holder.image);
        holder.deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("User")
                        .child(authUid)
                        .child("cart")
                        .child(model.getProductid())
                        .removeValue();
            }
        });
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlist, parent, false);
        return new CartAdapter.MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, size, price, id;
        ImageView image;
        Button deleteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ProductName);
            size = itemView.findViewById(R.id.ProductSize);
            price = itemView.findViewById(R.id.ProductPrice);
            image = itemView.findViewById(R.id.imageView);
            deleteBtn = itemView.findViewById(R.id.deleteItem);
            id = itemView.findViewById(R.id.id);
        }
    }
}

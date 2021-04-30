package com.example.swiftwipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class InformationAdapter extends FirebaseRecyclerAdapter<Information, InformationAdapter.MyViewHolder> {
    private static ClickListener clickListener;


    public InformationAdapter(@NonNull FirebaseRecyclerOptions<Information> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Information model) {
        holder.name.setText(model.getProductName());
        holder.size.setText(model.getProductSize());
        holder.price.setText("€"+model.getProductPrice());
        holder.id.setText(model.getProductid());
        Glide.with(holder.image.getContext()).load(model.getProductImage()).into(holder.image);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.informationlist, parent, false);
        return new InformationAdapter.MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, size, price, id;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.ProductName);
            size = itemView.findViewById(R.id.ProductSize);
            price = itemView.findViewById(R.id.ProductPrice);
            image = itemView.findViewById(R.id.imageView);
            id = itemView.findViewById(R.id.id);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }



    public void setOnItemClickListener(ClickListener clickListener) {
        InformationAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}

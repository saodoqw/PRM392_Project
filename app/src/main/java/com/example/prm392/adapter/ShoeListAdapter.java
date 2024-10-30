package com.example.prm392.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.entity.Product;
import com.example.prm392.DetailActivity;


import java.util.ArrayList;
import java.util.List;

public class ShoeListAdapter extends RecyclerView.Adapter<ShoeListAdapter.ShoeViewHolder> {

    private Context context;
    private List<Product> shoeList;
    private List<Product> filteredShoeList;

    public ShoeListAdapter(Context context, List<Product> shoeList) {
        this.context = context;
        this.shoeList = shoeList;
        this.filteredShoeList = new ArrayList<>(shoeList);
    }

    @NonNull
    @Override
    public ShoeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shoe, parent, false);
        return new ShoeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoeViewHolder holder, int position) {
        Product shoe = filteredShoeList.get(position);
        holder.shoeName.setText(shoe.getProductName());
        holder.shoePrice.setText("$" + String.valueOf(shoe.getPrice()));
        holder.shoeImage.setImageBitmap(BitmapFactory.decodeFile(shoe.getImageSrc()));
        // Gán sự kiện click vào itemView
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            // Truyền thông tin sản phẩm qua Intent
            intent.putExtra("productId", shoe.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredShoeList.size();
    }

    // Method to update the list
    public void updateList(List<Product> newList) {
        filteredShoeList.clear();
        filteredShoeList.addAll(newList);
        notifyDataSetChanged();
    }

    public static class ShoeViewHolder extends RecyclerView.ViewHolder {
        ImageView shoeImage;
        TextView shoeName;
        TextView shoePrice;

        public ShoeViewHolder(@NonNull View itemView) {
            super(itemView);
            shoeImage = itemView.findViewById(R.id.shoe_image);
            shoeName = itemView.findViewById(R.id.shoe_name);
            shoePrice = itemView.findViewById(R.id.shoe_price);
        }
    }
}


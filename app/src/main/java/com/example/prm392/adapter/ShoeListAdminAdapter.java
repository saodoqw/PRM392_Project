package com.example.prm392.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.entity.Product;
import com.example.prm392.entity.Shoe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ShoeListAdminAdapter extends RecyclerView.Adapter<ShoeListAdminAdapter.ShoeViewHolder> {

    private Context context;
    private List<Product> productList;
    private List<Product> filteredProductList;

    public ShoeListAdminAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.filteredProductList = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public ShoeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shoe_admin, parent, false);
        return new ShoeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoeViewHolder holder, int position) {
        Product product = filteredProductList.get(position);
        holder.shoeName.setText(product.getProductName());
        holder.shoePrice.setText("$" + String.valueOf(product.getPrice()));
        holder.shoeImage.setImageBitmap(BitmapFactory.decodeFile(product.getImageSrc()));
    }

    @Override
    public int getItemCount() {
        return filteredProductList.size();
    }

    // Method to update the list
    public void updateList(List<Product> newList) {
        filteredProductList.clear();
        filteredProductList.addAll(newList);
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


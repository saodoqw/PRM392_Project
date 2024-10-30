package com.example.prm392.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.UpdateShoeActivity;
import com.example.prm392.entity.Product;

import java.util.ArrayList;
import java.util.List;


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
        holder.update.setOnClickListener(v->{
            Intent intent = new Intent(context, UpdateShoeActivity.class);
            // Truyền thông tin sản phẩm qua Intent
            intent.putExtra("productId", product.getId());
            context.startActivity(intent);
        });
        holder.delete.setOnClickListener(v->{
            //delete
        });
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
        Button update;
        Button delete;

        public ShoeViewHolder(@NonNull View itemView) {
            super(itemView);
            shoeImage = itemView.findViewById(R.id.shoe_image);
            shoeName = itemView.findViewById(R.id.shoe_name);
            shoePrice = itemView.findViewById(R.id.shoe_price);
            update = itemView.findViewById(R.id.btn_update);
            delete = itemView.findViewById(R.id.btn_delete);

        }
    }

}


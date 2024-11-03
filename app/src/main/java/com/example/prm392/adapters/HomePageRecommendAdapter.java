package com.example.prm392.adapters;

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

import java.util.List;

public class HomePageRecommendAdapter extends RecyclerView.Adapter<HomePageRecommendAdapter.RecommendViewHolder> {
    private List<Product> recommendList;

    public HomePageRecommendAdapter(List<Product> recommendList) {
        this.recommendList = recommendList;
    }

    @NonNull
    @Override
    public RecommendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recommend, parent, false);
        return new RecommendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendViewHolder holder, int position) {
        Product product = recommendList.get(position);
        holder.productName.setText(product.getProductName());
        holder.productPrice.setText(String.valueOf(product.getPrice()));
        //get product image from drawable
        holder.productImage.setImageBitmap(BitmapFactory.decodeFile(product.getImageSrc()));
    }

    @Override
    public int getItemCount() {
        return recommendList.size();
    }

    static class RecommendViewHolder extends RecyclerView.ViewHolder {
        TextView productName;
        TextView productPrice;
        ImageView productImage;

        public RecommendViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.titleTxt);
            productPrice = itemView.findViewById(R.id.priceTxt);
            productImage = itemView.findViewById(R.id.pic);
        }
    }
}

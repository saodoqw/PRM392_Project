package com.example.prm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.entity.Brand;

import java.util.List;

public class HomePageBrandAdapter extends RecyclerView.Adapter<HomePageBrandAdapter.BrandViewHolder> {
    private final List<Brand> brandList;

    public HomePageBrandAdapter(List<Brand> brandList) {
        this.brandList = brandList;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        Brand brand = brandList.get(position);
        holder.brandName.setText(brand.getBrandName());
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    static class BrandViewHolder extends RecyclerView.ViewHolder {
        TextView brandName;

        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);
            brandName = itemView.findViewById(R.id.brandName);
        }
    }
}

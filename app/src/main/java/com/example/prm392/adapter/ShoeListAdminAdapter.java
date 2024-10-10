package com.example.prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.entity.Shoe;

import java.util.ArrayList;
import java.util.List;


public class ShoeListAdminAdapter extends RecyclerView.Adapter<ShoeListAdminAdapter.ShoeViewHolder> {

    private Context context;
    private List<Shoe> shoeList;
    private List<Shoe> filteredShoeList;

    public ShoeListAdminAdapter(Context context, List<Shoe> shoeList) {
        this.context = context;
        this.shoeList = shoeList;
        this.filteredShoeList = new ArrayList<>(shoeList);
    }

    @NonNull
    @Override
    public ShoeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_shoe_admin, parent, false);
        return new ShoeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoeViewHolder holder, int position) {
        Shoe shoe = filteredShoeList.get(position);
        holder.shoeName.setText(shoe.getName());
        holder.shoePrice.setText("$" + String.valueOf(shoe.getPrice()));
        holder.shoeImage.setImageResource(shoe.getImageResource());
    }

    @Override
    public int getItemCount() {
        return filteredShoeList.size();
    }

    // Method to update the list
    public void updateList(List<Shoe> newList) {
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


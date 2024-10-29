package com.example.prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.CartActivity;
import com.example.prm392.R;
import com.example.prm392.entity.DTO.ProductInCartWithQuantity;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<ProductInCartWithQuantity> cartItems;

    public CartAdapter(Context context, List<ProductInCartWithQuantity> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    public void setCartItems(List<ProductInCartWithQuantity> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        ProductInCartWithQuantity cartItem = cartItems.get(position);
        holder.nameTextView.setText(cartItem.product.getProductName());
        holder.quantityTextView.setText(String.valueOf(cartItem.getTotalQuantity()));
        holder.priceTextView.setText(String.valueOf(cartItem.product.getPrice()));
        holder.totalPriceTextView.setText(String.valueOf(cartItem.getTotalQuantity()*cartItem.product.getPrice()));
//       holder.productImageView.setImageResource((cartItem.getImageProductSrc());
        holder.increaseButton.setOnClickListener(v -> {
            if (context instanceof CartActivity) {
                ((CartActivity) context).increaseProductQuantity(cartItem);
            }        });

        holder.decreaseButton.setOnClickListener(v -> {
            if (context instanceof CartActivity) {
                ((CartActivity) context).decreaseProductQuantity(cartItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, quantityTextView, priceTextView, totalPriceTextView;
        TextView increaseButton, decreaseButton;
        ImageView productImageView;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.titleTxt);
            quantityTextView = itemView.findViewById(R.id.numberItem);
            priceTextView = itemView.findViewById(R.id.feeEachItem);
            totalPriceTextView = itemView.findViewById(R.id.totalEachItem);
            increaseButton = itemView.findViewById(R.id.plusCartItem);
            decreaseButton = itemView.findViewById(R.id.minusCartItem);
            productImageView = itemView.findViewById(R.id.itemPic);
        }
    }
}
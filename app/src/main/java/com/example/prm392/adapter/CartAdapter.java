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
import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.entity.DTO.ProductInCartWithQuantity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<ProductInCartWithQuantity> cartItems;
    private AppDatabase appDatabase;
    private ExecutorService executorService;

    public CartAdapter(Context context, List<ProductInCartWithQuantity> cartItems, AppDatabase appDatabase, ExecutorService executorService) {
        this.context = context;
        this.cartItems = cartItems;
        this.appDatabase = appDatabase;
        this.executorService = executorService;
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
        holder.totalPriceTextView.setText(String.valueOf(cartItem.getTotalQuantity() * cartItem.product.getPrice()));
        // holder.productImageView.setImageResource(cartItem.product.getImageSrc());

        executorService.execute(() -> {
            String color = getColor(cartItem.getColor());
            String size = getSize(cartItem.getSize());
            holder.colorTextView.post(() -> holder.colorTextView.setText(color));
            holder.sizeTextView.post(() -> holder.sizeTextView.setText(size));
        });

        holder.increaseButton.setOnClickListener(v -> {
            if (context instanceof CartActivity) {
                ((CartActivity) context).increaseProductQuantity(cartItem);
            }
        });

        holder.decreaseButton.setOnClickListener(v -> {
            if (context instanceof CartActivity) {
                ((CartActivity) context).decreaseProductQuantity(cartItem);
            }
        });
    }

    String getColor(long colorId) {
        return appDatabase.colorDao().getColorById(colorId).getColor();
    }

    String getSize(long sizeId) {
        return String.valueOf(appDatabase.sizeDao().getSizeBySizeId((int) sizeId).getSize());
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, quantityTextView, priceTextView, totalPriceTextView, colorTextView, sizeTextView;
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
            colorTextView = itemView.findViewById(R.id.txtColor);
            sizeTextView = itemView.findViewById(R.id.txt_size);
        }
    }
}
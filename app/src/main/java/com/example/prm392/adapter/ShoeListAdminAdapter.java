package com.example.prm392.adapter;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.AddShoeActivity;
import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.ShoeListAdminActivity;
import com.example.prm392.UpdateShoeActivity;
import com.example.prm392.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ShoeListAdminAdapter extends RecyclerView.Adapter<ShoeListAdminAdapter.ShoeViewHolder> {

    private Context context;
//    private List<Product> productList;
    private List<Product> filteredProductList;

    public ShoeListAdminAdapter(Context context, List<Product> productList) {
        this.context = context;
//        this.productList = productList;
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
        Log.d("ShoeListAdminAdapter", "Image path: " + product.getImageSrc());

        holder.shoeImage.setImageBitmap(BitmapFactory.decodeFile(product.getImageSrc()));

        holder.update.setOnClickListener(v->{
            Intent intent = new Intent(context, UpdateShoeActivity.class);
            // Truyền thông tin sản phẩm qua Intent
            intent.putExtra("productId", product.getId());
            ((Activity) context).startActivityForResult(intent, 1); // ĐÚNG
              // Request code 1
        });
        holder.delete.setOnClickListener(v->{
            // Tạo một AlertDialog
            new AlertDialog.Builder(context)
                    .setTitle("ARE YOU SURE?")
                    .setMessage("Do you really want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Xóa item ở đây
                        int positionDelte = holder.getAdapterPosition(); // Lấy vị trí của item
                        if (positionDelte != RecyclerView.NO_POSITION) {
                            // Gọi phương thức để xóa item từ danh sách dữ liệu
                            filteredProductList.remove(positionDelte); // Giả sử sizes là danh sách dữ liệu
                            notifyItemRemoved(positionDelte); // Thông báo xóa item

                            AppDatabase appDatabase = AppDatabase.getAppDatabase(context);
                            Executor executor = Executors.newSingleThreadExecutor();
                            executor.execute(() -> {
                                appDatabase.productDao().deleteProduct(product);
                            });
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss(); // Đóng hộp thoại nếu chọn "No"
                    })
                    .show(); // Hiển thị hộp thoại
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


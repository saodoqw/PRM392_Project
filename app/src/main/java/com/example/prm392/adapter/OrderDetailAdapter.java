package com.example.prm392.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.activities.OrderDetailActivity;
import com.example.prm392.entity.OrderDetail;
import com.example.prm392.entity.Product;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderDetailAdapter extends ArrayAdapter<OrderDetail> {

    private Context context;
    private AppDatabase appDatabase;

    public OrderDetailAdapter(Context context, List<OrderDetail> orderDetails, AppDatabase appDatabase) {
        super(context, 0, orderDetails);
        this.context = context;
        this.appDatabase = appDatabase;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.viewholder_order_detail, parent, false);
        }

        OrderDetail detail = getItem(position);

        TextView name = convertView.findViewById(R.id.name);
        ImageView productImg = convertView.findViewById(R.id.product_img);
        TextView amount = convertView.findViewById(R.id.amount);
        TextView unitPrice = convertView.findViewById(R.id.unit_price);
        TextView totalPrice = convertView.findViewById(R.id.total_price);
        TextView finalPrice = convertView.findViewById(R.id.final_price);

        setProductDetail(detail, name, productImg);
        amount.setText("x" + detail.getQuantity());
        unitPrice.setText("đ" + detail.getUnitPrice());
        int total = detail.getQuantity() * detail.getUnitPrice();
        totalPrice.setText("đ" + total);
        finalPrice.setText("đ" + total);

        return convertView;
    }

    public void setProductDetail(OrderDetail orderDetails, TextView name, ImageView productImg) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            long productId = orderDetails.getProductId();
            Product product = appDatabase.productDao().getProductById((int) productId);

            ((Activity) context).runOnUiThread(() -> {
                if (product != null) {
                    name.setText(product.getProductName());
//                    String imageName = product.getImageSrc();
//                    int imageResource = getImageResource(imageName);
//                    productImg.setImageResource(imageResource);
                } else {
                    Toast.makeText(context, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
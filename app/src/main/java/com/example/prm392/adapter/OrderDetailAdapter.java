package com.example.prm392.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
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
import com.example.prm392.entity.Brand;
import com.example.prm392.entity.Color;
import com.example.prm392.entity.OrderDetail;
import com.example.prm392.entity.Product;
import com.example.prm392.entity.Size;

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
        TextView brand = convertView.findViewById(R.id.brand);
        TextView color = convertView.findViewById(R.id.color);
        TextView size = convertView.findViewById(R.id.size);
        TextView amount = convertView.findViewById(R.id.amount);
        TextView unitPrice = convertView.findViewById(R.id.unit_price);
        TextView totalPrice = convertView.findViewById(R.id.total_price);
        TextView finalPrice = convertView.findViewById(R.id.final_price);

        setProductDetail(detail, name, productImg, brand, color, size);
        amount.setText("x" + detail.getQuantity());
        unitPrice.setText("đ" + detail.getUnitPrice());
        int total = detail.getQuantity() * detail.getUnitPrice();
        totalPrice.setText("đ" + total);
        finalPrice.setText("đ" + total);

        return convertView;
    }

    public void setProductDetail(OrderDetail orderDetails, TextView name, ImageView productImg, TextView brand, TextView color, TextView size) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            long productId = orderDetails.getProductId();
            long sizeID = orderDetails.getSizeId();
            long colorId = orderDetails.getColorId();
            Product product = appDatabase.productDao().getProductById((int) productId);
            long brandId = product.getBrandId();
            String image = appDatabase.imageShoeDao().getFirstImageByProductId((int) productId);
            String brandName = appDatabase.brandDao().getBrandNameById(brandId);
            Size shoeSize = appDatabase.sizeDao().getSizeBySizeId((int) sizeID);
            Color shoeColor = appDatabase.colorDao().getColorById((int) colorId);

            ((Activity) context).runOnUiThread(() -> {
                if (product != null) {
                    name.setText(product.getProductName());
                    productImg.setImageBitmap(BitmapFactory.decodeFile(image));
                    brand.setText(brandName);
                    color.setText(shoeColor.getColor());
                    size.setText(String.valueOf(shoeSize.getSize()));
                } else {
                    Toast.makeText(context, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
package com.example.prm392.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.R;
import com.example.prm392.activities.OrderDetailActivity;
import com.example.prm392.entity.OrderDetail;

import java.util.List;

public class OrderDetailAdapter extends ArrayAdapter<OrderDetail> {

    private Context context;

    public OrderDetailAdapter(Context context, List<OrderDetail> orderDetails) {
        super(context, 0, orderDetails);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.viewholder_order_detail, parent, false);
        }

        OrderDetail detail = getItem(position);

        // Khai báo các TextView
        TextView amount = convertView.findViewById(R.id.amount);
        TextView unitPrice = convertView.findViewById(R.id.unit_price);
        TextView totalPrice = convertView.findViewById(R.id.total_price);
        TextView finalPrice = convertView.findViewById(R.id.final_price);

        // Hiển thị thông tin
        amount.setText("x" + detail.getQuantity());
        unitPrice.setText("đ" + detail.getUnitPrice());
        int total = detail.getQuantity() * detail.getUnitPrice();
        totalPrice.setText("đ" + total);
        finalPrice.setText("đ" + total);

        return convertView;
    }
}
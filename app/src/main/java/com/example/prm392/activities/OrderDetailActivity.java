package com.example.prm392.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.adapter.OrderDetailAdapter;

public class OrderDetailActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        setContentView(R.layout.activity_order_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listView = findViewById(R.id.detail_item_list);
        Button updateButton = findViewById(R.id.btn_update);
        Button cancelButton = findViewById(R.id.btn_cancel);
        TextView status = findViewById(R.id.status);
        long orderId = getIntent().getLongExtra("orderId", -1);

        if (orderId != -1) {
            appDatabase.orderDetailDao()
                    .getOrderDetailByOrderId(orderId)
                    .observe(this, details -> {
                        if (details != null) {
                            OrderDetailAdapter adapter = new OrderDetailAdapter(this, details);
                            listView.setAdapter(adapter);
                        } else {
                            Toast.makeText(this, "Không có dữ liệu cho đơn hàng", Toast.LENGTH_SHORT).show();
                        }
                    });
            appDatabase.orderDao().getOrderById(orderId)
                            .observe(this, order -> {
                                String orderStatus = order.getStatus();
                                if (orderStatus != null) {
                                    status.setText("Order is " + orderStatus);

                                    status.setBackgroundColor(Color.TRANSPARENT);
                                    switch (orderStatus) {
                                        case "Completed":
                                            updateButton.setVisibility(View.GONE);
                                            cancelButton.setVisibility(View.GONE);
                                            status.setBackgroundColor(0xFF33CC99);
                                            break;
                                        case "Cancelled":
                                            updateButton.setVisibility(View.GONE);
                                            cancelButton.setVisibility(View.GONE);
                                            status.setBackgroundColor(0xFFCC0000);
                                            break;
                                        default:
                                            status.setBackgroundColor(0xFFFF6600);
                                            updateButton.setOnClickListener(v -> {
                                                Intent intent = new Intent(this, UpdateOrderStatusActivity.class);
                                                intent.putExtra("orderId", orderId);
                                                startActivity(intent);
                                            });
                                            break;
                                    }
                                }
                            });
        } else {
            Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
        }

        cancelOrder(cancelButton, updateButton, status, orderId);

    }

    protected void cancelOrder(Button cancelButton, Button updateButton, TextView status, long orderId) {
        cancelButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cancel Order")
                    .setMessage("Are you sure to cancel this order?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        new Thread(() -> {
                            appDatabase.orderDao().updateOrderStatus(orderId, "Cancelled");

                            runOnUiThread(() -> {
                                Toast.makeText(this, "Order has been cancelled", Toast.LENGTH_SHORT).show();
                                cancelButton.setVisibility(View.GONE);
                                updateButton.setVisibility(View.GONE);
                                status.setText("Order is Cancelled");
                                status.setBackgroundColor(0xFFCC0000);
                            });
                        }).start();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }
}
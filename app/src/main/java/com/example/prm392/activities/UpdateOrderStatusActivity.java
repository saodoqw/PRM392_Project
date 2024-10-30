package com.example.prm392.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;

public class UpdateOrderStatusActivity extends AppCompatActivity {

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_order_status);
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button updateButton = findViewById(R.id.btn_update);
        RadioButton pendingRadio = findViewById(R.id.rdb_pending);
        RadioButton processingRadio = findViewById(R.id.rdb_processing);
        RadioButton completedRadio = findViewById(R.id.rdb_completed);
        RadioButton shippingRadio = findViewById(R.id.rdb_shipping);
        ImageView backButton = findViewById(R.id.backBtn);

        long orderId = getIntent().getLongExtra("orderId", -1);

        if (orderId != -1) {
            appDatabase.orderDao().getOrderById(orderId).observe(this, order -> {
                if (order != null) {
                    switch (order.getStatus()) {
                        case "Pending":
                            pendingRadio.setChecked(true);
                            break;
                        case "Processing":
                            processingRadio.setChecked(true);
                            break;
                        case "Shipping":
                            shippingRadio.setChecked(true);
                            break;
                        case "Completed":
                            completedRadio.setChecked(true);
                            break;
                    }
                } else {
                    Toast.makeText(this, "Order not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

        updateButton.setOnClickListener(v -> {
            String newStatus = null;

            if (pendingRadio.isChecked()) {
                newStatus = "Pending";
            } else if (processingRadio.isChecked()) {
                newStatus = "Processing";
            } else if (completedRadio.isChecked()) {
                newStatus = "Completed";
            } else if (shippingRadio.isChecked()) {
                newStatus = "Shipping";
            }

            if (newStatus != null && orderId != -1) {
                updateOrderStatus(orderId, newStatus);
            } else {
                Toast.makeText(this, "Please select order status", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra("orderId", orderId);
            startActivity(intent);
        });

    }

    private void updateOrderStatus(long orderId, String status) {
        new Thread(() -> {
            try {
                appDatabase.orderDao().updateOrderStatus(orderId, status);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Order status has been updated", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error updating status", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }
}
package com.example.prm392.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditShippingDetailActivity extends AppCompatActivity {

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_shipping_detail);
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText address = findViewById(R.id.new_address);
        Button saveButton = findViewById(R.id.btn_save);
        ImageView backButton = findViewById(R.id.backBtn);
        long orderId = getIntent().getLongExtra("orderId", -1);

        if (orderId != -1) {

            appDatabase.orderDao().getOrderById(orderId)
                    .observe(this, order -> {
                        address.setText(order.getShippingAddress());
                    });
        } else {
            Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
        }

        saveButton.setOnClickListener(v -> {
            String newAddress = address.getText().toString().trim();
            if (!newAddress.isEmpty()) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    appDatabase.orderDao().updateShippingAddress(orderId, newAddress);

                    runOnUiThread(() ->
                            Toast.makeText(this, "Đã lưu địa chỉ mới", Toast.LENGTH_SHORT).show()
                    );
                });
            } else {
                Toast.makeText(this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderDetailActivity.class);
            intent.putExtra("orderId", orderId);
            startActivity(intent);
            finish();
        });
    }
}
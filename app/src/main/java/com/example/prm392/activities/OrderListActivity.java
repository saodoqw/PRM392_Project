package com.example.prm392.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.adapter.OrderListAdapter;
import com.example.prm392.entity.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderListActivity extends AppCompatActivity {

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_list);
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listView = findViewById(R.id.detail_item_list);
        loadOrders(listView);

        ImageView backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, OrderListActivity.class);
            startActivity(intent);
        });
    }

    private void loadOrders(ListView listView) {
        OrderListAdapter adapter = new OrderListAdapter(OrderListActivity.this, new ArrayList<>(), appDatabase);
        listView.setAdapter(adapter);

        appDatabase.orderDao().getAllOrders().observe(this, orders -> {
            if (orders != null) {
                adapter.setOrders(orders);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(OrderListActivity.this, "Không có đơn hàng nào", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
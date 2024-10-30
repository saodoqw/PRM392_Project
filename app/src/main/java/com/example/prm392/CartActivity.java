package com.example.prm392;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392.adapter.CartAdapter;
import com.example.prm392.entity.Cart;
import com.example.prm392.entity.CartItem;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.cart), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        cart = new Cart();
//        cartRecyclerView = findViewById(R.id.ViewCart);
//        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        cartAdapter = new CartAdapter(this, cart.getItems());
//        cartRecyclerView.setAdapter(cartAdapter);
//
//        // Add sample cart items
//        cart.addItem(new CartItem(1, "Product 1", 2, 10.0));
//        cart.addItem(new CartItem(2, "Product 2", 1, 20.0));
//        cartAdapter.notifyDataSetChanged();

        //get back button
        findViewById(R.id.backBtn).setOnClickListener(view -> finish());
    }
}
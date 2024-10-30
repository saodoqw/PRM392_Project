package com.example.prm392;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.adapter.CartAdapter;
import com.example.prm392.entity.Cart;
import com.example.prm392.entity.Coupon;
import com.example.prm392.entity.DTO.ProductInCartWithQuantity;
import com.example.prm392.entity.Order;
import com.example.prm392.entity.OrderDetail;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private AppDatabase appDatabase;
    private ExecutorService executorService;
    private TextView txtCartEmpty;
    private TextView txtSubTotalPrice;
    private Button btnCheckout;
    private EditText eCoupon;
    private TextView txtTotal;
    private TextView txtDiscount;
    private Long coupponId;

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

        txtCartEmpty = findViewById(R.id.cartEmptyTxt);
        txtSubTotalPrice = findViewById(R.id.subtotalTxt);
        btnCheckout = findViewById(R.id.btnCheckout);
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        executorService = Executors.newSingleThreadExecutor(); // Initialize executorService
        initViewCart();
        loadProductsInCart();
        // Set OnClickListener for the Apply button
        eCoupon = findViewById(R.id.couponTxt);
        txtTotal = findViewById(R.id.totalTxt);
        txtDiscount = findViewById(R.id.promotionTxt);
        Button btnApplyCoupon = findViewById(R.id.applyCouponBtn);
        btnApplyCoupon.setOnClickListener(v -> {
            applyCoupon();
        });
         ImageView backBtn= findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            finish();
        });
        btnCheckout.setOnClickListener(v -> {
            if (cartAdapter.getItemCount() == 0) {
                Toast.makeText(CartActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }
            double totalAmount = Double.parseDouble(txtTotal.getText().toString().replace("$", ""));
            Intent intent = new Intent(CartActivity.this, CheckOutActivity.class);
            if (coupponId != null && coupponId != 0) {
                intent.putExtra("COUPON_ID", coupponId);
            }
            intent.putExtra("TOTAL_AMOUNT", totalAmount);
            startActivity(intent);
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(cartUpdateReceiver,
                new IntentFilter("com.example.prm392.CART_UPDATED"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(cartUpdateReceiver);
    }

    private final BroadcastReceiver cartUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadProductsInCart();

        }
    };

    private void disableApplyButton(Button button) {
        new Handler(Looper.getMainLooper()).post(() -> button.setEnabled(false));
    }

    private void enableApplyButton(Button button) {
        new Handler(Looper.getMainLooper()).post(() -> button.setEnabled(true));
    }

    private void initViewCart() {
        cartRecyclerView = findViewById(R.id.ViewCart);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(this, new ArrayList<>(), appDatabase, executorService); // Initialize Adapter with an empty list
        cartRecyclerView.setAdapter(cartAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadProductsInCart();
        }
    }

    private void loadProductsInCart() {
        executorService.execute(() -> {
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
            int accountId = sharedPreferences.getInt("ACCOUNT_ID", -1);
            if (accountId == -1) {
                return;
            }
            List<ProductInCartWithQuantity> pList = appDatabase.cartDao()
                    .getProductsInCartGroupedByAccountId(1); // Get products in cart
            new Handler(Looper.getMainLooper()).post(() -> {
                if (pList.isEmpty()) {
                    txtCartEmpty.setVisibility(View.VISIBLE);
                } else {
                    txtCartEmpty.setVisibility(View.GONE);
                }
                cartAdapter.setCartItems(pList); // Update the list of products in the Adapter
                cartAdapter.notifyDataSetChanged(); // Notify the Adapter to update the data
                // Calculate subtotal
                double subtotal = 0;
                for (ProductInCartWithQuantity product : pList) {
                    subtotal += product.getTotalQuantity() * product.product.getPrice();
                }
                enableApplyButton(findViewById(R.id.applyCouponBtn));
                txtTotal.setText(String.format("$%.2f", subtotal));
                txtSubTotalPrice.setText(String.format("$%.2f", subtotal)); // Update subtotal TextView
            });
        });
    }

    public void increaseProductQuantity(ProductInCartWithQuantity Product) {
        executorService.execute(() -> {
            Cart cartItem = appDatabase.cartDao().getCartItemByProductIdSizeColor(Product.product.getId(),Product.size,Product.color);
            if (cartItem != null) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                appDatabase.cartDao().updateCart(cartItem);
            }
            loadProductsInCart();
        });
    }


    public void decreaseProductQuantity(ProductInCartWithQuantity Product) {
        executorService.execute(() -> {
            Cart cartItem = appDatabase.cartDao().getCartItemByProductIdSizeColor(Product.product.getId(),Product.size,Product.color);
            if (cartItem != null && cartItem.getQuantity() > 1) {
                cartItem.setQuantity(cartItem.getQuantity() - 1);
                appDatabase.cartDao().updateCart(cartItem);
            } else if (cartItem != null && cartItem.getQuantity() == 1) {
                appDatabase.cartDao().deleteCart(cartItem);
            }
            loadProductsInCart();
        });
    }


    private double discount = 0.0;

    public void applyCoupon() {
        executorService.execute(() -> {
            List<Coupon> coupons = appDatabase.couponDao().getAllCoupons();
            if (coupons.isEmpty()) {
                new Handler(Looper.getMainLooper()).post(() ->
                        Toast.makeText(this, "Don't have any coupons yet", Toast.LENGTH_SHORT).show()
                );
                return;
            }
            if (eCoupon == null || eCoupon.getText().toString().trim().isEmpty()) {
                new Handler(Looper.getMainLooper()).post(() ->
                        eCoupon.setError("Please enter a coupon code")
                );
                return;
            }
            String enteredCouponCode = eCoupon.getText().toString().trim();

            boolean isCouponValid = false;
            for (Coupon coupon : coupons) {
                System.out.println("Database Coupon Code: " + coupon.getCouponCode());
                if (coupon.getCouponCode().equals(enteredCouponCode)) {
                    if (coupon.getId() == 1) {

                    } else {
                        isCouponValid = true;
                        java.util.Date currentDate = new java.sql.Date(System.currentTimeMillis());
                        int errorDisplayDuration = 2000;

                        if (coupon.getStartDate().after(currentDate) && coupon.getEndDate().before(currentDate)) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                eCoupon.setError("Coupon is expired");
                                new Handler(Looper.getMainLooper()).postDelayed(() -> eCoupon.setError(null), errorDisplayDuration);
                            });
                            return;
                        }
                        if (coupon.getUsageCount() >= coupon.getUsageLimit()) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                eCoupon.setError("Coupon usage limit reached");
                                new Handler(Looper.getMainLooper()).postDelayed(() -> eCoupon.setError(null), errorDisplayDuration);
                            });
                            return;
                        }
                        double minOrderValue = coupon.getMinOrderValue();
                        System.out.println("Minimum order value from DB: " + minOrderValue);
                        if (Double.parseDouble(txtSubTotalPrice.getText().toString().replace("$", "")) < minOrderValue && coupon.getMaxOrderValue() > Double.parseDouble(txtSubTotalPrice.getText().toString().replace("$", ""))) {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                eCoupon.setError("Minimum order value is " + minOrderValue + " and maximum order value is " + coupon.getMaxOrderValue());
                                new Handler(Looper.getMainLooper()).postDelayed(() -> eCoupon.setError(null), errorDisplayDuration);
                            });
                            return;
                        }
                        new Handler(Looper.getMainLooper()).post(() -> {
                            coupponId = coupon.getId();
                            discount = coupon.getDiscountValue();
                            txtDiscount.setText(String.format("$%.2f", discount));
                            double subtotal = Double.parseDouble(txtSubTotalPrice.getText().toString().replace("$", ""));
                            double newTotal = subtotal - discount;
                            txtTotal.setText(String.format("$%.2f", newTotal));
                            disableApplyButton(findViewById(R.id.applyCouponBtn)); // Disable the button here
                        });
                        break;
                    }
                }
            }

            if (!isCouponValid) {
                new Handler(Looper.getMainLooper()).post(() -> eCoupon.setError("Invalid coupon code"));
            }
        });

    }
}
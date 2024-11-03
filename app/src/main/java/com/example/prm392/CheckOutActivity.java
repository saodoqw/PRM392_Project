package com.example.prm392;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.entity.Coupon;
import com.example.prm392.entity.DTO.ProductInCartWithQuantity;
import com.example.prm392.entity.Order;
import com.example.prm392.entity.OrderDetail;
import com.example.prm392.entity.ProductQuantity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class CheckOutActivity extends AppCompatActivity {
    private TextView tvTotalAmount;
    private RadioGroup rgPaymentMethods;
    private EditText etCardNumber, etCardholderName, etExpirationDate, etAddress;
    private Button btnConfirmPayment;
    private ImageView backBtn;
    private int accountId ;
    private AppDatabase appDatabase;
    private ExecutorService executorService;
    private AtomicReference<Long> orderId = new AtomicReference<>();
    private Long couponId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        rgPaymentMethods = findViewById(R.id.rgPaymentMethods);
        etCardNumber = findViewById(R.id.etCardNumber);
        etCardholderName = findViewById(R.id.etCardholderName);
        etExpirationDate = findViewById(R.id.etExpirationDate);
        etAddress = findViewById(R.id.etAddress);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
        List<ProductInCartWithQuantity> cartItems = new ArrayList<>();
        couponId = getIntent().getLongExtra("COUPON_ID", 1);

        // Retrieve the total coupon value and total amount from the Intent
        double totalAmount = getIntent().getDoubleExtra("TOTAL_AMOUNT", 0.0);
//        Object op= getIntent().getSerializableExtra("Account");
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTotalAmount.setText("Total Amount: $" + totalAmount);

        rgPaymentMethods.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbDirect) {
                    etCardNumber.setVisibility(View.GONE);
                    etCardholderName.setVisibility(View.GONE);
                    etExpirationDate.setVisibility(View.GONE);
                    etAddress.setVisibility(View.VISIBLE);
                } else {
                    etCardNumber.setVisibility(View.VISIBLE);
                    etCardholderName.setVisibility(View.VISIBLE);
                    etExpirationDate.setVisibility(View.VISIBLE);
                    etAddress.setVisibility(View.VISIBLE);
                }
            }
        });

        btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleQuantity(cartItems);
                processPayment();
            }
        });
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
         accountId = sharedPreferences.getInt("ACCOUNT_ID", -1);
        if (accountId == -1) {
            return;
        }
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        executorService = Executors.newSingleThreadExecutor(); // Initialize executorService
        executorService.execute(() -> {
            cartItems.addAll(appDatabase.cartDao().getProductsInCartGroupedByAccountId(accountId));
        });
    }

    private void handleQuantity(List<ProductInCartWithQuantity> cartItems) {
        for (ProductInCartWithQuantity cartItem : cartItems) {
            executorService.execute(() -> {
                int quantity = getQuantity(cartItem);
                updateQuantity(cartItem, quantity);
            });
        }
    }

    private int getQuantity(ProductInCartWithQuantity cartItem) {
        return appDatabase.productQuantityDAO().getQuantity(cartItem.product.getId(), cartItem.size, cartItem.color);
    }

    private void updateQuantity(ProductInCartWithQuantity cartItem, int quantity) {
        appDatabase.productQuantityDAO().updateQuantity(cartItem.product.getId(), cartItem.size, cartItem.color, quantity - cartItem.totalQuantity);
    }

    private void processPayment() {
        int selectedPaymentMethodId = rgPaymentMethods.getCheckedRadioButtonId();
        if (selectedPaymentMethodId == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedPaymentMethodId == R.id.rbCreditCard) {
            String cardNumber = etCardNumber.getText().toString();
            String cardholderName = etCardholderName.getText().toString();
            String expirationDate = etExpirationDate.getText().toString();
            String address = etAddress.getText().toString();
            if (cardNumber.isEmpty() || cardholderName.isEmpty() || expirationDate.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill in all payment information", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (selectedPaymentMethodId == R.id.rbDirect) {
            String address = etAddress.getText().toString();
            if (address.isEmpty()) {
                Toast.makeText(this, "Please fill in the delivery address", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Process the payment (this is a placeholder, actual implementation will vary)
        boolean paymentSuccess = true; // Assume payment is successful

        if (paymentSuccess) {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();

            addOrder();
            addOrderDetail();
            if (couponId != null && couponId != 1) {
                addUsageCount();
            }
            addUsageCount();
            deleteCart();
        } else {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void addUsageCount() {
        executorService.execute(() -> {
            if (couponId != null && couponId != 1) {
                Coupon coupon = appDatabase.couponDao().getCouponById(couponId);
                if (coupon != null) {
                    coupon.setUsageCount(coupon.getUsageCount() + 1);
                    appDatabase.couponDao().updateCoupon(coupon);
                }
            }
        });
    }

    private void addOrder() {
        CountDownLatch latch = new CountDownLatch(1);

        executorService.execute(() -> {
            double totalAmount = getIntent().getDoubleExtra("TOTAL_AMOUNT", 0.0);
            Order order = new Order(0, System.currentTimeMillis(), null, null, null, null, null,
                    System.currentTimeMillis(), (int) totalAmount, "Pending", 0, accountId, etAddress.getText().toString());
            orderId.set(appDatabase.orderDao().insertOrder(order));
            latch.countDown();
        });

        executorService.execute(() -> {
            try {
                latch.await();
                addOrderDetail();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    private void addOrderDetail() {
        if (orderId.get() != null && orderId.get() > 0) {
            List<ProductInCartWithQuantity> cartItems = appDatabase.cartDao().getProductsInCartGroupedByAccountId(accountId);
            for (ProductInCartWithQuantity cartItem : cartItems) {
                long sizeId = cartItem.size;
                long colorId = cartItem.color;

                OrderDetail orderDetail = new OrderDetail(0, null, null, null, null, null, null,
                        cartItem.totalQuantity, (int) cartItem.product.getPrice(), orderId.get(), cartItem.product.getId(), couponId, sizeId, colorId);
                appDatabase.orderDetailDao().insertOrderDetail(orderDetail);
            }
        }
    }

    // CheckOutActivity.java
    private void deleteCart() {
        executorService.execute(() -> {
            appDatabase.cartDao().deleteCartByAccountId(accountId);
            runOnUiThread(() -> {
                // Notify CartActivity to reload the cart
                Intent intent = new Intent("com.example.prm392.CART_UPDATED");
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                finish();
            });
        });
    }
}
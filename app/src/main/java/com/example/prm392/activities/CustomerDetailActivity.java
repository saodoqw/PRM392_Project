package com.example.prm392.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.entity.Account;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomerDetailActivity extends AppCompatActivity {
    private int CustomerID;
    private AppDatabase appDatabase;
    private ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.customerDetail), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        executorService = Executors.newSingleThreadExecutor();
        CustomerID = getIntent().getIntExtra("CustomerID", -1); // Ensure CustomerID is retrieved
        if(CustomerID != -1){
            loadCustomerDetail();
        }
        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadCustomerDetail() {
        executorService.execute(() -> {
            Account customer = appDatabase.accountDao().getAccountById((int)CustomerID);
            runOnUiThread(() -> {
                TextView txtName = findViewById(R.id.username);
                TextView txtPhone = findViewById(R.id.phone);
                TextView txtAdress = findViewById(R.id.address);
                TextView txtCreateAte = findViewById(R.id.CreateAt);
                TextView txtName1 = findViewById(R.id.textview);
                txtName1.setText(customer.getUsername());
                txtName.setText(customer.getUsername());
                txtPhone.setText(customer.getPhone());
                txtAdress.setText(customer.getAddress());
                txtCreateAte.setText(customer.getCreatedAt().toString());
                ImageView img = findViewById(R.id.customerImg);
                if (customer.getImage() != null) {
                    img.setImageBitmap(BitmapFactory.decodeFile(customer.getImage()));
                } else {
                    img.setImageResource(R.drawable.user_profile);
                }            });
        });

    }
}
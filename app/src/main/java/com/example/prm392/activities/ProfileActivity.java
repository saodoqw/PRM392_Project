package com.example.prm392.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.entity.Account;

import java.sql.Date;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {

    TextView username;
    TextView address;
    TextView phone;
    TextView role;
    TextView joinDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //set data for views
        username = findViewById(R.id.userName);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        role = findViewById(R.id.role);
        joinDate = findViewById(R.id.joinAt);
        //get data from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        int accountId = sharedPreferences.getInt("ACCOUNT_ID", 0);
        //Initialize Room database
        AppDatabase appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        //get data from database

        Executors.newSingleThreadExecutor().execute(() -> {
            Account account = appDatabase.accountDao().getAccountById(accountId);
            String roleName = appDatabase.roleDao().getRoleById(account.getUserRoleId()).getRoleName().toString();
            //set data for views
            runOnUiThread(() -> {
                address.setText(account.getAddress());
                phone.setText(account.getPhone());
                role.setText(roleName);
                username.setText(account.getUsername());
                Date date = new Date(account.getCreatedAt());
                joinDate.setText(date.toString());
            });
        });
        //return to homepage
        findViewById(R.id.backBtn).setOnClickListener(v -> {
            finish();
        });
    }
}

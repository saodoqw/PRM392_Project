package com.example.prm392.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.entity.Account;

import java.sql.Date;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText phone;
    private EditText address;
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //Initialize views
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        Button registerButton = findViewById(R.id.register);
        //Initialize Room database
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        //Set click listener for register button
        registerButton.setOnClickListener(view -> handleRegister(username.getText().toString().trim(),
                password.getText().toString().trim(), phone.getText().toString().trim(),
                address.getText().toString().trim()));
        //Set click listener for login button
        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void handleRegister(String username, String password, String phone, String address) {
        //Check if username, password, phone, and address are empty
        if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            //Show error message
            runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show());
            return;
        }
        //Check if username already exists
        Executors.newSingleThreadExecutor().execute(() -> {
            Account account = appDatabase.accountDao().checkUsername(username);
            if (account != null) {
                //Show error message
                runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show());
            } else {
                account = new Account(0, System.currentTimeMillis(), null,
                        null, "System", null, null, username, password, phone,
                        address, null, 2);
                try {
                    //Insert account to database
                    appDatabase.accountDao().insert(account);
                    //Show success message
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Register successful!", Toast.LENGTH_SHORT).show());
                    // Change layout to activity_login.xml
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class); //Modify to navigate to login page
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    //Show error message
                    runOnUiThread(() -> Toast.makeText(RegisterActivity.this, "Register failed!", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}

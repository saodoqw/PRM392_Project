package com.example.prm392.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.entity.Account;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private AppDatabase appDatabase;

    // This method is called when the activity is first created.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Initialize views
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login);
        //Initialize Room database
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        //Set click listener for login button
        loginButton.setOnClickListener(view -> handleLogin(username.getText().toString().trim(), password.getText().toString().trim()));
        //Set click listener for register button
        Button registerButton = findViewById(R.id.register);
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        //Set click listener for forgot password button
        Button forgotPasswordButton = findViewById(R.id.forgotPassword);
        forgotPasswordButton.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin(String username, String password) {
        //Check if username and password are empty
        if (username.isEmpty() || password.isEmpty()) {
            runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show());
            return;
        }
        //Handle login in background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            Account account = appDatabase.accountDao().checkLogin(username, password);
            if (account != null) {
                //Log success message
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show());
                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int accountid = (int) account.getId();
                int roleid = (int) account.getUserRoleId();
                editor.putInt("ACCOUNT_ID",accountid);
                editor.putInt("ROLE_ID", roleid);
                editor.apply();
                // Change layout to activity_homePage.xml
//                Intent intent = new Intent(LoginActivity.this, CartActivity.class); //Modify to navigate to home page
                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class); //Modify to navigate to home page
                startActivity(intent);
                finish();
            } else {
                //Log error message
                runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show());
            }
        });
    }
}

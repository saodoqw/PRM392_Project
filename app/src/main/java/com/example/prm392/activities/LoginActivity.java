package com.example.prm392.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.MainActivity;
import com.example.prm392.R;
import com.example.prm392.entity.Account;

import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private AppDatabase appDatabase;

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
        loginButton.setOnClickListener(view -> {
            handleLogin(username.getText().toString().trim(), password.getText().toString().trim());
        });
    }

    private void handleLogin(String username, String password) {
        //Check if username and password are empty
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        //Handle login in background thread
        Executors.newSingleThreadExecutor().execute(() -> {
            Account account = appDatabase.accountDao().checkLogin(username, password);
            if (account != null) {
                Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                // Change layout to activity_homePage.xml
                Intent intent = new Intent(LoginActivity.this, MainActivity.class); //Modify to navigate to home page
                startActivity(intent);
                finish();
            } else {
                //Log error message
                Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
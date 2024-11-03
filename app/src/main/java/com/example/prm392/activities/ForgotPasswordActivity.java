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

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Initialize views
        EditText phone = findViewById(R.id.phone);
        //Initialize Room database
        AppDatabase appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        //Set click listener for submit button
        findViewById(R.id.resetPassword).setOnClickListener(view -> {
            //Check if phone is empty
            if (phone.getText().toString().isEmpty()) {
                runOnUiThread(() -> Toast.makeText(ForgotPasswordActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show());
                return;
            }
            //Handle submit in background thread
            Executors.newSingleThreadExecutor().execute(() -> {
                //Check if phone exists in database
                Account account = appDatabase.accountDao().checkPhone(phone.getText().toString().trim());
                if (account != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("ACCOUNT_ID", (int)account.getId());
                    editor.apply();
                    //Redirect to reset password activity
                    Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);

                    startActivity(intent);
                } else {
                    //Log error message
                    runOnUiThread(() -> Toast.makeText(ForgotPasswordActivity.this, "Phone does not exist", Toast.LENGTH_SHORT).show());
                }
            });
        });
        //Set click listener for back button
        Button backButton = findViewById(R.id.login);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}

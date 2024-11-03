package com.example.prm392.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.entity.Account;

import java.util.concurrent.Executors;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        AppDatabase appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        //Initialize views
        EditText newPassword = findViewById(R.id.newPassword);
        EditText rePassword = findViewById(R.id.rePassword);
        //check if new password and re-enter password are the same
        findViewById(R.id.resetPassword).setOnClickListener(view -> {
            if (newPassword.getText().toString().equals(rePassword.getText().toString())) {
                //get id from shared preferences
                int id = getSharedPreferences("account", MODE_PRIVATE).getInt("ACCOUNT_ID", 0);
                //reset password in database
                Executors.newSingleThreadExecutor().execute(() -> {
                    //get account from database
                    Account account = appDatabase.accountDao().getAccountById(id);
                    account.setPassword(newPassword.getText().toString());
                    //update password in database
                    appDatabase.accountDao().updatePassword(account.getId(),account.getPassword());
                    //Log success message
                    runOnUiThread(() -> Toast.makeText(ResetPasswordActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show());
                    //Redirect to login activity
                    Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(intent);
                });
            } else {
                //Log error message
                Toast.makeText(ResetPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });
        //Set click listener for back button
        findViewById(R.id.login).setOnClickListener(view -> {
            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}

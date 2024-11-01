package com.example.prm392.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.adapter.PolicyAdapter;
import com.example.prm392.entity.Policy;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PolicyActivity extends AppCompatActivity {

    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_policy);
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ListView listView = findViewById(R.id.policy_list);
        loadPolicies(listView);

        ImageView backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
            finish();
        });

    }

    private void loadPolicies(ListView listView) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Policy> policy = appDatabase.policyDao().getAllPolicy();

            runOnUiThread(() -> {
                if (policy != null) {
                    PolicyAdapter adapter = new PolicyAdapter(this, policy, appDatabase);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(this, "Không tìm thấy tài khoản", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
package com.example.prm392;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392.adapter.ActivityAdapter;
import com.example.prm392.entity.Activity;
import java.util.ArrayList;
import java.util.List;

public class CustomerDetailActivity extends AppCompatActivity {
    private RecyclerView activityRecyclerView;
    private ActivityAdapter activityAdapter;
    private List<Activity> activityList;

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

        activityRecyclerView = findViewById(R.id.activity_customer_detail);
        activityRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        activityList = new ArrayList<>();
        // Add some sample activities
        activityList.add(new Activity("Activity 1", "Description for Activity 1"));
        activityList.add(new Activity("Activity 2", "Description for Activity 2"));
        activityList.add(new Activity("Activity 3", "Description for Activity 3"));
        activityList.add(new Activity("Activity 4", "Description for Activity 4"));
        activityList.add(new Activity("Activity 5", "Description for Activity 5"));
        activityList.add(new Activity("Activity 6", "Description for Activity 6"));
        activityList.add(new Activity("Activity 7", "Description for Activity 7"));
        activityList.add(new Activity("Activity 8", "Description for Activity 8"));
        activityList.add(new Activity("Activity 9", "Description for Activity 9"));
        activityList.add(new Activity("Activity 10", "Description for Activity 10"));

        activityAdapter = new ActivityAdapter(activityList);
        activityRecyclerView.setAdapter(activityAdapter);
    }
}
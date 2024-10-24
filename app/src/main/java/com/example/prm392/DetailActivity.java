package com.example.prm392;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.adapter.ColorListAdapter;
import com.example.prm392.adapter.SizeListAdapter;
import com.example.prm392.entity.Color;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //List color
        List<Color> colors = new ArrayList<Color>();
        colors.add(new Color(1,"Black"));
        colors.add(new Color(2,"White"));
        colors.add(new Color(3,"Grey"));



        RecyclerView recyclerView = findViewById(R.id.colorList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ColorListAdapter(getApplicationContext(),colors));


        //List size
        List<Integer> sizes = new ArrayList<>();
        for (int i = 36; i <= 45; i++) {
            sizes.add(i);  // Add sizes from 36 to 45
        }
        RecyclerView recyclerViewSize = findViewById(R.id.sizeList); // Assuming colorList is the RecyclerView for size
        recyclerViewSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSize.setAdapter(new SizeListAdapter(getApplicationContext(), sizes));  // Use SizeListAdapter for sizes

        //Handle back button
        // Tìm ImageView với id backBtn
        ImageView backBtn = findViewById(R.id.backBtn);
        // Gán sự kiện OnClickListener
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, ShoeListActivity.class);
            startActivity(intent);
        });

    }
}
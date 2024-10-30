package com.example.prm392.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm392.CartActivity;
import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.adapter.HomePageAdapter;
import com.example.prm392.adapter.HomePageBrandAdapter;
import com.example.prm392.entity.Brand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class HomePageActivity extends AppCompatActivity {
    private AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        //set image for imageviewSlider
        ViewPager2 viewPager = findViewById(R.id.viewPageSlider);

        ArrayList<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.banner1);
        imageList.add(R.drawable.banner2);

        //set adapter for viewpager
        HomePageAdapter imageAdapter = new HomePageAdapter(this, imageList);
        viewPager.setAdapter(imageAdapter);

        //Set content for Official brand
        RecyclerView brandRecyclerView = findViewById(R.id.viewBrand);
        //Initialize Room database
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        List<Brand> brandList = new ArrayList<>();
        //get data from database
        Executors.newSingleThreadExecutor().execute(() -> {
            brandList.addAll(appDatabase.brandDao().getAll());
        });
        //set adapter for recyclerview
        HomePageBrandAdapter brandAdapter = new HomePageBrandAdapter(brandList);
        brandRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        brandRecyclerView.setAdapter(brandAdapter);

        //Get imageview id from layout
        ImageView discovery = findViewById(R.id.discovery);
        ImageView profile = findViewById(R.id.profile);
        ImageView cart = findViewById(R.id.cart);
        ImageView order = findViewById(R.id.order);

        //Set click listener for each imageview
        discovery.setOnClickListener(view -> {
            //Navigate to home page
            Intent intent = new Intent(HomePageActivity.this, HomePageActivity.class);
            startActivity(intent);
        });
        profile.setOnClickListener(view -> {
            //Navigate to profile page
//            Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
//            startActivity(intent);
        });
        cart.setOnClickListener(view -> {
            //Navigate to cart page
            Intent intent = new Intent(HomePageActivity.this, CartActivity.class);
            startActivity(intent);
        });
        order.setOnClickListener(view -> {
            //Navigate to order page
//            Intent intent = new Intent(HomePageActivity.this, OrderActivity.class);
//            startActivity(intent);
        });
    }
}

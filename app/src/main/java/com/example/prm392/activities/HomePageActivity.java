package com.example.prm392.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm392.activities.CartActivity;
import com.example.prm392.activities.CustomerActivity;
import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.activities.ShoeListActivity;
import com.example.prm392.adapters.HomePageAdapter;
import com.example.prm392.adapters.HomePageBrandAdapter;
import com.example.prm392.adapters.HomePageRecommendAdapter;
import com.example.prm392.adapters.HomePageAdapter;
import com.example.prm392.adapters.HomePageBrandAdapter;
import com.example.prm392.adapters.HomePageRecommendAdapter;
import com.example.prm392.entity.Account;
import com.example.prm392.entity.Brand;
import com.example.prm392.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HomePageActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    List<Product> products = new ArrayList<>();
    HomePageRecommendAdapter recommendAdapter;
    RecyclerView viewRecommend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_homepage);
        TextView seeAllRecommend = findViewById(R.id.seeAllRecommend);
        //Initialize Room database
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        //Set content for user name
        TextView userName = findViewById(R.id.userName);
        //Get id from share preference
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        int accountId = sharedPreferences.getInt("ACCOUNT_ID", 0);

        //Get account from database
        Executors.newSingleThreadExecutor().execute(() -> {
            Account user = appDatabase.accountDao().getAccountById(accountId);
            //Set user name for textview after getting data
            runOnUiThread(() -> {
                userName.setText(user.getUsername());
                if(user.getUserRoleId() == 1){
                    seeAllRecommend.setOnClickListener(view -> {
                        //Navigate to product page
                        Intent intent = new Intent(HomePageActivity.this, ShoeListAdminActivity.class);
                        startActivityForResult(intent, 1);
                    });
                }else{
                    seeAllRecommend.setOnClickListener(view -> {
                        //Navigate to product page
                        Intent intent = new Intent(HomePageActivity.this, ShoeListActivity.class);
                        startActivity(intent);
                    });
                }
            });
            if (user.getUserRoleId() == 1) {
                ImageView btnToLisCustomer = findViewById(R.id.imageView6);
                btnToLisCustomer.setVisibility(ImageView.VISIBLE);
                btnToLisCustomer.setOnClickListener(view -> {
                    Intent intent = new Intent(HomePageActivity.this, CustomerActivity.class);
                    startActivity(intent);
                });
            }
        });

        SetData();


        //Get imageview id from layout
        ImageView discovery = findViewById(R.id.discovery);
        ImageView profile = findViewById(R.id.profile);
        ImageView cart = findViewById(R.id.cart);
        ImageView order = findViewById(R.id.order);


        //Set click listener for each imageview
        discovery.setOnClickListener(view -> {
            //Navigate to policy
            Intent intent = new Intent(HomePageActivity.this, PolicyActivity.class);
            startActivity(intent);
        });
        profile.setOnClickListener(view -> {
            //Navigate to profile page
            Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        cart.setOnClickListener(view -> {
            //Navigate to cart page
            Intent intent = new Intent(HomePageActivity.this, CartActivity.class);
            startActivity(intent);
        });
        order.setOnClickListener(view -> {
            //Navigate to order page
            Intent intent = new Intent(HomePageActivity.this, OrderListActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        SetData();
    }


    private void SetData(){
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

        List<Brand> brandList = new ArrayList<>();
        //get data from database
        Executors.newSingleThreadExecutor().execute(() -> {
            brandList.addAll(appDatabase.brandDao().getAll());
        });
        //set adapter for recyclerview
        HomePageBrandAdapter brandAdapter = new HomePageBrandAdapter(brandList);
        brandRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        brandRecyclerView.setAdapter(brandAdapter);
        //Hide progress bar if official brand is loaded
        findViewById(R.id.progressBar2).setVisibility(ImageView.GONE);
        //Set data for product

        viewRecommend = findViewById(R.id.viewRecommend);
        //get all product from database
        Executors.newSingleThreadExecutor().execute(() -> {
            products.addAll(appDatabase.productDao().getAllProducts());
        });
        //set adapter for viewpager
        recommendAdapter = new HomePageRecommendAdapter(products);
        viewRecommend.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        // set adapter for recyclerview
        viewRecommend.setAdapter(recommendAdapter);
        //Hide progress bar if product is loaded
        findViewById(R.id.progressBarPopular).setVisibility(ImageView.GONE);
        //Get imageview id from layout
        ImageView discovery = findViewById(R.id.discovery);
        ImageView profile = findViewById(R.id.profile);
        ImageView cart = findViewById(R.id.cart);
        ImageView order = findViewById(R.id.order);

        //Set click listener for each imageview
        discovery.setOnClickListener(view -> {
            //Navigate to policy
            Intent intent = new Intent(HomePageActivity.this, PolicyActivity.class);
            startActivity(intent);
        });
        profile.setOnClickListener(view -> {
            //Navigate to profile page
            Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
        cart.setOnClickListener(view -> {
            //Navigate to cart page
            Intent intent = new Intent(HomePageActivity.this, CartActivity.class);
            startActivity(intent);
        });
        order.setOnClickListener(view -> {
            //Navigate to order page
            Intent intent = new Intent(HomePageActivity.this, OrderListActivity.class);
            startActivity(intent);
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Refresh the RecyclerView by re-fetching the product list
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                products = appDatabase.productDao().getAllProducts();  // Re-fetch products
                runOnUiThread(() -> {
                    recommendAdapter = new HomePageRecommendAdapter(products);
                    viewRecommend.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
                    // set adapter for recyclerview
                    viewRecommend.setAdapter(recommendAdapter);
                });
            });
        }
    }
}

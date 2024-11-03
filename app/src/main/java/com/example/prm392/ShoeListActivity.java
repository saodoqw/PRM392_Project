package com.example.prm392;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.activities.HomePageActivity;
import com.example.prm392.adapter.ShoeListAdapter;
import com.example.prm392.entity.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ShoeListActivity extends AppCompatActivity {

    private ShoeListAdapter adapter;
    private EditText searchBar;
    private String selectedBrand = "All";  // Default to "All"
    private String selectedPriceOption = "Price";  // Default to "Price"
    private String searchQuery = "";  // Default to an empty string (no search)

    private List<Product> productList;
    private AppDatabase appDatabase;
    List<String> brands = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoe_list);
        ImageView cartBtn = findViewById(R.id.cartBtn);
        cartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ShoeListActivity.this, CartActivity.class);
            startActivity(intent);
        });

        Spinner brandSpinner = findViewById(R.id.brand_spinner);
        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.shoe_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            //Get a list of shoes
            productList = appDatabase.productDao().getAllProducts();
            brands.addAll(appDatabase.brandDao().getAllBrand());
            runOnUiThread(() -> {
                // Set up the adapter
                adapter = new ShoeListAdapter(this, productList);
                recyclerView.setAdapter(adapter);

                //Set up brands spinner
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brands);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brandSpinner.setAdapter(spinnerAdapter);
            });
        });

        //turn off keyboard when dont use
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    hideKeyboard(recyclerView);  // Hide the keyboard when scrolling starts
                }
            }
        });
        // Set up price sort filter
        Spinner priceSortSpinner = findViewById(R.id.price_sort_spinner);
        List<String> priceOptions = new ArrayList<>();
        priceOptions.add("Price");  // Add "Price" as the default option
        priceOptions.add("Ascending");
        priceOptions.add("Descending");

        ArrayAdapter<String> priceSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priceOptions);
        priceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceSortSpinner.setAdapter(priceSpinnerAdapter);

        // Price filter selection
        priceSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPriceOption = priceOptions.get(position);  // Update selected price filter
                applyFilters();  // Apply all filters
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Set up search bar
        searchBar = findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString();  // Update search query
                applyFilters();  // Apply all filters
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //setup default brand
        brands.add("All");
        // Handle data


        // Brand filter selection
        brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBrand = brands.get(position);  // Update selected brand
                applyFilters();  // Apply all filters
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //Handle back button
        // Tìm ImageView với id backBtn
        ImageView backBtn = findViewById(R.id.backBtn);
        // Gán sự kiện OnClickListener
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ShoeListActivity.this, HomePageActivity.class);
            startActivity(intent);
            finish();
        });

    }

    // Method to apply all filters
    private void applyFilters() {
        List<Product> filteredList = new ArrayList<>();
        // Filter by brand
        for (Product product : productList) {
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                String brandOfProduct = appDatabase.brandDao().getBrandNameById(product.getBrandId());
                if (selectedBrand.equals("All") || brandOfProduct.equalsIgnoreCase(selectedBrand)) {
                    if (product.getProductName().toLowerCase().contains(searchQuery.toLowerCase())) {
                        filteredList.add(product);
                    }
                }
                runOnUiThread(() -> {
                    // Sắp xếp danh sách theo giá
                    sortByPrice(filteredList);
                    adapter.updateList(filteredList);
                });
            });
        }
    }

    // Hàm sắp xếp danh sách theo giá
    private void sortByPrice(List<Product> productList) {
        if (selectedPriceOption.equals("Ascending")) {
            Collections.sort(productList, Comparator.comparingDouble(Product::getPrice));
        } else if (selectedPriceOption.equals("Descending")) {
            Collections.sort(productList, (s1, s2) -> Double.compare(s2.getPrice(), s1.getPrice()));
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}

package com.example.prm392.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.entity.Account;
import com.example.prm392.adapters.CustomerAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CustomerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private List<Account> customerList;
    private List<Account> filteredCustomerList;
    private AppDatabase appDatabase;
    private ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.customerList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView back = findViewById(R.id.backBtn);
        back.setOnClickListener(v -> finish());
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        executorService = Executors.newSingleThreadExecutor(); // Initialize executorService
        initCustomerList();
        loadCustomers();

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void initCustomerList() {
        recyclerView = findViewById(R.id.cusList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        customerAdapter = new CustomerAdapter( new ArrayList<>(),this);
        recyclerView.setAdapter(customerAdapter);

    }


    private void loadCustomers() {
        executorService.execute(() -> {
            customerList = appDatabase.accountDao().getAccountsUser();
            filteredCustomerList = new ArrayList<>(customerList);
            runOnUiThread(() -> {
                customerAdapter.setCustomerList(filteredCustomerList);
                customerAdapter.notifyDataSetChanged();
                TextView noCustomerTextView = findViewById(R.id.noCustomerTextView);
                if (filteredCustomerList.isEmpty()) {
                    noCustomerTextView.setVisibility(View.VISIBLE);
                } else {
                    noCustomerTextView.setVisibility(View.GONE);
                }
            });
        });
    }

    private void filter(String text) {
        filteredCustomerList.clear();
        if (text.isEmpty()) {
            filteredCustomerList.addAll(customerList);
        } else {
            filteredCustomerList.addAll(customerList.stream()
                    .filter(customer -> customer.getUsername().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        customerAdapter.notifyDataSetChanged();
    }
}
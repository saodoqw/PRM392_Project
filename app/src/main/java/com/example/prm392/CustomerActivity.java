package com.example.prm392;

import android.os.Bundle;
import android.widget.SearchView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392.entity.Customer;
import com.example.prm392.adapter.CustomerAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private List<Customer> customerList;
    private List<Customer> filteredCustomerList;

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

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        customerList = new ArrayList<>();
        // Add some sample customers
        customerList.add(new Customer(1, "John Doe", "john@example.com", "123 Main St"));
        customerList.add(new Customer(2, "Jane Smith", "jane@example.com", "456 Elm St"));

        filteredCustomerList = new ArrayList<>(customerList);
        customerAdapter = new CustomerAdapter(filteredCustomerList);
        recyclerView.setAdapter(customerAdapter);

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

    private void filter(String text) {
        filteredCustomerList.clear();
        if (text.isEmpty()) {
            filteredCustomerList.addAll(customerList);
        } else {
            filteredCustomerList.addAll(customerList.stream()
                    .filter(customer -> customer.getName().toLowerCase().contains(text.toLowerCase()))
                    .collect(Collectors.toList()));
        }
        customerAdapter.notifyDataSetChanged();
    }
}
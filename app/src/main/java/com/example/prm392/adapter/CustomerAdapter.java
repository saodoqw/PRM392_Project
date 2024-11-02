package com.example.prm392.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent; // Import Intent

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.CustomerDetailActivity;
import com.example.prm392.R;
import com.example.prm392.entity.Account;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private List<Account> customerList;
    private Context context;

    public CustomerAdapter(List<Account> customerList, Context context) {
        this.customerList = customerList;
        this.context = context;
    }

    public void setCustomerList(List<Account> customerList) {
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Account customer = customerList.get(position);
        holder.nameTextView.setText(customer.getUsername());
        holder.phoneTextView.setText(customer.getPhone());
        holder.addressTextView.setText(customer.getAddress());
        if (customer.getImage() != null) {
            holder.customerImageView.setImageBitmap(BitmapFactory.decodeFile(customer.getImage()));
        } else {
            holder.customerImageView.setImageResource(R.drawable.user_profile);
        }
        holder.bind(customer, context);
    }



    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public Account getCustomerAt(int position) {
        return customerList.get(position);
    }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;
        TextView addressTextView;
        ImageView customerImageView;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.customer_name);
            phoneTextView = itemView.findViewById(R.id.customer_phone);
            addressTextView = itemView.findViewById(R.id.customer_address);
            customerImageView = itemView.findViewById(R.id.cusImg);
        }

        public void bind(Account customer, Context context) {
            itemView.setOnClickListener(v -> {
                int customerId = (int)customer.getId();
                if (customerId != -1) { // Ensure CustomerID is valid
                    itemView.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.grey));
                    Toast.makeText(itemView.getContext(), "Click on " + customerId, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, CustomerDetailActivity.class);
                    intent.putExtra("CustomerID", customerId); // Ensure CustomerID is set
                    context.startActivity(intent);
                    itemView.postDelayed(() -> {
                        itemView.setBackgroundColor(itemView.getContext().getResources().getColor(android.R.color.transparent));
                    }, 200); // 300ms
                } else {
                    Toast.makeText(itemView.getContext(), "Invalid CustomerID", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
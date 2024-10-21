package com.example.prm392.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.entity.Activity;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.CustomerActivityViewHolder> {
    private List<Activity> activityList;

    public ActivityAdapter(List<Activity> activityList) {
        this.activityList = activityList;
    }

    @NonNull
    @Override
    public CustomerActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_customer_detail, parent, false);
        return new CustomerActivityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerActivityViewHolder holder, int position) {
        Activity activity = activityList.get(position);
        holder.activityTitle.setText(activity.getTitle());
        holder.activityDescription.setText(activity.getDescription());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class CustomerActivityViewHolder extends RecyclerView.ViewHolder {
        public TextView activityTitle;
        public TextView activityDescription;

        public CustomerActivityViewHolder(View itemView) {
            super(itemView);
            activityTitle = itemView.findViewById(R.id.activity_title);
            activityDescription = itemView.findViewById(R.id.activity_description);
        }
    }
}
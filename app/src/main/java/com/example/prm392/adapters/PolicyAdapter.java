package com.example.prm392.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.prm392.Data.AppDatabase;
import com.example.prm392.R;
import com.example.prm392.entity.Policy;

import java.util.List;

public class PolicyAdapter extends ArrayAdapter<Policy> {

    private AppDatabase appDatabase;
    private Context context;

    public PolicyAdapter(Context context, List<Policy> policies, AppDatabase appDatabase) {
        super(context, 0, policies);
        this.context = context;
        this.appDatabase = appDatabase;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.viewholder_policy, parent, false);
        }

        Policy policy =  getItem(position);

        TextView content = convertView.findViewById(R.id.tv_policy);

        content.setText(policy.getContent());
        return convertView;
    }

}


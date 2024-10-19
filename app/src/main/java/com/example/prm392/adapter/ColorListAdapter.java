package com.example.prm392.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.entity.Color;

import java.util.List;

public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.ColorListViewHolder> {

    private Context context;
    private List<Color> colors;
    private int selectedPosition = RecyclerView.NO_POSITION; // Initial no selection

    public ColorListAdapter(Context context, List<Color> colors) {
        this.context = context;
        this.colors = colors;
    }
    @NonNull
    @Override
    public ColorListAdapter.ColorListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_color, parent,false);

        return new ColorListViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull ColorListAdapter.ColorListViewHolder holder, int position) {
        Color color = colors.get(position);
        holder.pic.setImageResource(color.getColor());

        // Check if the current position matches the selected position
        if (holder.getAdapterPosition() == selectedPosition) {
            // Set the purple border for the selected item
            holder.pic.setBackgroundResource(R.drawable.selected_border);
        } else {
            // Remove the border for unselected items
            holder.pic.setBackgroundResource(R.drawable.grey_bg);
        }

        // Set click listener to update the selected position
        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            // Update selected position
            notifyItemChanged(selectedPosition); // Notify previous selected item to remove the border
            selectedPosition = currentPosition;
            notifyItemChanged(selectedPosition); // Notify new selected item to add the border
        });
    }

    @Override
    public int getItemCount() {
        return colors.size();  // Return the size of the colors list
    }

    public class ColorListViewHolder extends RecyclerView.ViewHolder {
        private ImageView pic;

        public ColorListViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
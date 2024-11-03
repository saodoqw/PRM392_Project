package com.example.prm392.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
//import androidx.annotation.Size;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.entity.Size;

import java.util.List;

public class SizeListAdapter extends RecyclerView.Adapter<SizeListAdapter.SizeListViewHolder> {

    private String selectedColor;
    private String selectedSize;
    private Context context;
    private List<Size> sizes;
    private int selectedPosition = RecyclerView.NO_POSITION; // Initial no selection
    private OnSizeClickListener listener;
    public interface OnSizeClickListener {
        void onSizeClick(int sizeId);
    }

    public SizeListAdapter(Context context, List<Size> sizes, SizeListAdapter.OnSizeClickListener listener) {
        this.context = context;
        this.sizes = sizes;
        this.listener = listener;
    }
    @NonNull
    @Override
    public SizeListAdapter.SizeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_size, parent,false);

        return new SizeListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SizeListAdapter.SizeListViewHolder holder, int position) {
        Size size = sizes.get(position);
        holder.sizetxt.setText("" + (int)size.getSize());

        // Check if the current position matches the selected position
        if (holder.getAdapterPosition() == selectedPosition) {
            // Set the purple border for the selected item
            holder.itemView.setBackgroundResource(R.drawable.selected_border);
        } else {
            // Remove the border for unselected items
            holder.itemView.setBackgroundResource(R.drawable.grey_bg);
        }

        // Set click listener to update the selected position
        holder.itemView.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            // Update selected position
            notifyItemChanged(selectedPosition); // Notify previous selected item to remove the border
            selectedPosition = currentPosition;
            notifyItemChanged(selectedPosition); // Notify new selected item to add the border

            listener.onSizeClick((int)size.getId());
        });
    }


    @Override
    public int getItemCount() {
        return sizes.size();  // Return the size of the colors list
    }

    public class SizeListViewHolder extends RecyclerView.ViewHolder {
        private TextView sizetxt;

        public SizeListViewHolder(@NonNull View itemView) {
            super(itemView);
            sizetxt = itemView.findViewById(R.id.sizeTxt);
        }
    }
}

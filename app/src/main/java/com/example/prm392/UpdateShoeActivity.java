package com.example.prm392;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateShoeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView shoeImage;
    private EditText shoeName, priceEditText;
    private Spinner brandSpinner;
    private LinearLayout stockContainer;
    private Button addSizeColorButton, updateShoeButton, changeImageButton;

    // Holds the size and color combinations
    private List<StockItem> stockList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shoe);

        // Initialize UI elements
        shoeImage = findViewById(R.id.shoe_image);
        shoeName = findViewById(R.id.edit_shoe_name);
        priceEditText = findViewById(R.id.edit_price);
        brandSpinner = findViewById(R.id.spinner_brand);
        stockContainer = findViewById(R.id.stock_container);
        addSizeColorButton = findViewById(R.id.btn_add_size_color);
        updateShoeButton = findViewById(R.id.btn_update_shoe);
        changeImageButton = findViewById(R.id.btn_change_image);

        // Handle image change
        changeImageButton.setOnClickListener(v -> {
            // Open gallery to select an image
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        // Handle adding size and color stock entries
        addSizeColorButton.setOnClickListener(v -> addSizeColorFields());

        // Handle shoe update
        updateShoeButton.setOnClickListener(v -> updateShoe());

    }

    // Handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                shoeImage.setImageBitmap(bitmap); // Set the selected image
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Add dynamic fields for size and color stock
    private void addSizeColorFields() {
        View sizeColorView = getLayoutInflater().inflate(R.layout.size_color_stock_item, null);

        EditText sizeEditText = sizeColorView.findViewById(R.id.edit_size);
//        EditText colorEditText = sizeColorView.findViewById(R.id.edit_color);
        EditText stockEditText = sizeColorView.findViewById(R.id.edit_stock);

        stockContainer.addView(sizeColorView);

        // Add this combination to stock list (data binding)
//        stockList.add(new StockItem(sizeEditText, colorEditText, stockEditText));

        stockList.add(new StockItem(sizeEditText, stockEditText));
    }

    // Handle updating the shoe details
    private void updateShoe() {
        String name = shoeName.getText().toString();
        String price = priceEditText.getText().toString();
        String brand = brandSpinner.getSelectedItem().toString();

        // Validate data
        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Please fill all the required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Handle stock update by size and color
        for (StockItem item : stockList) {
            String size = item.sizeEditText.getText().toString();
//            String color = item.colorEditText.getText().toString();
            String stock = item.stockEditText.getText().toString();

            // Validate size, color, stock
//            if (size.isEmpty() || color.isEmpty() || stock.isEmpty()) {

                if (size.isEmpty()  || stock.isEmpty()) {
                Toast.makeText(this, "Please fill all size/color/stock fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // You can update the product stock here based on size and color.
        }

        // Perform shoe update (database or API call)
        Toast.makeText(this, "Shoe updated successfully!", Toast.LENGTH_SHORT).show();
        // Finish activity or go back to previous screen
        finish();
    }

    // Inner class to hold stock item
    private class StockItem {
        EditText sizeEditText;
//        EditText colorEditText;
        EditText stockEditText;

        StockItem(EditText size, EditText stock) {
            this.sizeEditText = size;
//            this.colorEditText = color;
            this.stockEditText = stock;
        }
    }
}

package com.example.prm392;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392.R;
import com.example.prm392.adapter.ImageAdapter;
import com.example.prm392.entity.Color;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UpdateShoeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView shoeImage;
    private EditText shoeName, priceEditText;
    private Spinner brandSpinner,colorSpinner;
    private LinearLayout stockContainer;
    private Button updateShoeButton, changeImageButton, addColor;

    // Holds the size and color combinations
    private List<StockItem> stockList = new ArrayList<>();
    private List<Color> colors = new ArrayList<>();
    List<String> colorNames = new ArrayList<>();

    //Handle view image selected from gallery
    private RecyclerView imagesRecyclerView;
    private ImageAdapter imageAdapter;
    private List<Bitmap> selectedImages = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shoe);

        // Initialize UI elements
//        shoeImage = findViewById(R.id.shoe_image);
        shoeName = findViewById(R.id.edit_shoe_name);
        priceEditText = findViewById(R.id.edit_price);
        brandSpinner = findViewById(R.id.spinner_brand);
        stockContainer = findViewById(R.id.stock_container);
//        addSizeColorButton = findViewById(R.id.btn_add_size_color);
        updateShoeButton = findViewById(R.id.btn_update_shoe);
        changeImageButton = findViewById(R.id.btn_change_image);
        addColor = findViewById(R.id.btn_add_new_color);


        // Handle image change

        imagesRecyclerView = findViewById(R.id.images_recycler_view);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageAdapter = new ImageAdapter(this, selectedImages);
        imagesRecyclerView.setAdapter(imageAdapter);
        changeImageButton.setOnClickListener(v -> {
            // Mở gallery để chọn nhiều ảnh
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);  // Cho phép chọn nhiều ảnh
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        //Handle brand
        List<String> brands = new ArrayList<>();
        brands.add("Nike");
        brands.add("Adidas");
        brands.add("Puma");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brands);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(spinnerAdapter);



        // Handle adding size and color stock entries
//        addSizeColorButton.setOnClickListener(v -> addSizeColorFields());
        for (int i = 35; i <= 45; i++) {
            addSizeFields(i);
        }

        // Handle shoe update
        updateShoeButton.setOnClickListener(v -> updateShoe());

        // Handle add new color
        addColor.setOnClickListener(v -> addNewColor());





        //Handle back button
        // Tìm ImageView với id backBtn
        ImageView backBtn = findViewById(R.id.backBtn);

        // Gán sự kiện OnClickListener
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateShoeActivity.this, ShoeListActivity.class);
            startActivity(intent);
        });
    }

    private void addNewColor() {
        // Tạo AlertDialog để yêu cầu người dùng nhập tên màu
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm Màu Mới");

        // Tạo EditText cho người dùng nhập tên màu
        final EditText input = new EditText(this);
        input.setHint("Nhập tên màu...");
        input.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        builder.setView(input);

        // Xử lý sự kiện khi nhấn nút "Thêm"
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String newColorName = input.getText().toString().trim();

            if (!newColorName.isEmpty()) {
                // Thêm màu mới vào danh sách
//                colors.add(new Color(colors.size() + 1, newColorName));
                colorNames.add(newColorName);
                // Cập nhật lại adapter của Spinner
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) colorSpinner.getAdapter();
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Màu mới đã được thêm!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Tên màu không được để trống.", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút hủy bỏ
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        // Hiển thị AlertDialog
        builder.show();
    }

    // Handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    addScaledImage(imageUri);  // Load và thêm ảnh đã được nén
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                addScaledImage(imageUri);  // Load và thêm ảnh đã được nén
            }

            // Cập nhật RecyclerView sau khi thêm ảnh
            imageAdapter.notifyDataSetChanged();
        }
    }

    // Phương thức thêm ảnh với scaling để tránh OOM
    private void addScaledImage(Uri imageUri) {
        try {
            Bitmap bitmap = decodeSampledBitmapFromUri(imageUri, 300, 300); // Scale ảnh về 300x300
            if (bitmap != null) {
                selectedImages.add(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức load bitmap đã được scale từ Uri
    private Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {
        try {
            // Tùy chọn để lấy kích thước ảnh ban đầu
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream input = getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(input, null, options);
            input.close();

            // Tính toán tỉ lệ inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;

            // Load ảnh đã được nén
            input = getContentResolver().openInputStream(uri);
            Bitmap scaledBitmap = BitmapFactory.decodeStream(input, null, options);
            input.close();

            return scaledBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tính toán tỉ lệ nén ảnh
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight &&
                    (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    // Add dynamic fields for size and stock
    private void addSizeFields(int i) {
        // Inflate the layout for size and stock entry
        View sizeColorView = getLayoutInflater().inflate(R.layout.size_color_stock_item, null);

        // Find views by ID
        TextView sizeTextView = sizeColorView.findViewById(R.id.txt_size); // Ensure this is a TextView
        EditText stockEditText = sizeColorView.findViewById(R.id.edit_stock); // Ensure this is an EditText

        // Set the size number to the TextView
        sizeTextView.setText(String.valueOf(i));

        // Add the inflated view to the stock container
        stockContainer.addView(sizeColorView);

        // Add this combination to the stock list (data binding)
        stockList.add(new StockItem(sizeTextView, stockEditText));
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
            if (size.isEmpty()|| stock.isEmpty()) {
                Toast.makeText(this, "Please fill all size/stock fields.", Toast.LENGTH_SHORT).show();
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
        TextView sizeEditText;
        EditText stockEditText;

        StockItem(TextView size, EditText stock) {
            this.sizeEditText = size;
            this.stockEditText = stock;
        }
    }
}

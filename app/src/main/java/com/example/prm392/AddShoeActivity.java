package com.example.prm392;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.adapter.ImageAdapter;
import com.example.prm392.entity.Color;
import com.example.prm392.entity.ImageShoe;
import com.example.prm392.entity.Product;
import com.example.prm392.entity.ProductQuantity;
import com.example.prm392.entity.Size;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class AddShoeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView shoeImage;
    private EditText shoeName, priceEditText, descriptionEditText;
    private Spinner brandSpinner, colorSpinner;
    private LinearLayout stockContainer;
    private Button addSizeColorButton, updateShoeButton, changeImageButton, addColor;

    // Holds the size and color combinations
    private List<StockItem> stockList = new ArrayList<>();
    private List<Color> colors = new ArrayList<>();
    List<String> colorNames = new ArrayList<>();

    //Handle view image selected from gallery
    private RecyclerView imagesRecyclerView;
    private ImageAdapter imageAdapter;
    private List<Bitmap> selectedImages = new ArrayList<>();
    List<Size> sizes;
    List<String> brands;
    private AppDatabase appDatabase;

    String statusAdd;

    void setStatus(String statusAdd) {
        this.statusAdd = statusAdd;
    }

    String getStatus() {
        return statusAdd;
    }

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
        updateShoeButton = findViewById(R.id.btn_update_shoe);
        changeImageButton = findViewById(R.id.btn_change_image);
        addColor = findViewById(R.id.btn_add_new_color);
        descriptionEditText = findViewById(R.id.edit_description_shoe);
        //Handle add button
        updateShoeButton.setText("Add Shoe");

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
        // Handle adding size and color stock entries
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Thao tác với database ở background thread
            sizes = appDatabase.sizeDao().getAllSizes();
            brands = appDatabase.brandDao().getAllBrand();
            runOnUiThread(() -> {
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brands);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brandSpinner.setAdapter(spinnerAdapter);
            });
        });


        // Handle shoe update
        updateShoeButton.setOnClickListener(v -> updateShoe());

        // Handle add new color
        addColor.setOnClickListener(v -> addNewColor());


        //Handle back button
        // Tìm ImageView với id backBtn
        ImageView backBtn = findViewById(R.id.backBtn);
        // Gán sự kiện OnClickListener
        backBtn.setOnClickListener(v -> {
            finish();
        });

    }

    private void addNewColor() {
        // Tạo AlertDialog để yêu cầu người dùng nhập tên màu
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new color");

        // Tạo EditText cho người dùng nhập tên màu
        final EditText input = new EditText(this);
        input.setHint("Enter color...");
        input.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        builder.setView(input);

        // Xử lý sự kiện khi nhấn nút "Thêm"
        builder.setPositiveButton("Add", (dialog, which) -> {
            String newColorName = input.getText().toString().trim();

            if (!newColorName.isEmpty()) {
                // Thêm màu mới vào danh sách
//                colors.add(new Color(colors.size() + 1, newColorName));
                colorNames.add(newColorName);
                addSizeFields(newColorName);
                Toast.makeText(this, "Màu mới đã được thêm!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Color is not empty!", Toast.LENGTH_SHORT).show();
            }
        });

        // Nút hủy bỏ
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Hiển thị AlertDialog
        builder.show();
    }

    // Handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Reset selectedImages list nếu có ảnh được chọn
            selectedImages.clear();  // Xóa dữ liệu cũ

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    loadScaledImage(imageUri);  // Load ảnh đã scale
                }
            } else if (data.getData() != null) {
                Uri imageUri = data.getData();
                loadScaledImage(imageUri);  // Load ảnh đã scale
            }

            imageAdapter.notifyDataSetChanged();  // Cập nhật RecyclerView
        } else {
            Toast.makeText(this, "No images selected.", Toast.LENGTH_SHORT).show();
        }
    }

    // Load bitmap đã được scale từ Uri
    private void loadScaledImage(Uri uri) {
        try {
            Bitmap bitmap = decodeSampledBitmapFromUri(uri, 600, 600);  // Scale ảnh về 600x600
            if (bitmap != null) {
                selectedImages.add(bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Decode bitmap đã scale từ Uri
    private Bitmap decodeSampledBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {
        try {
            // Tùy chọn để chỉ lấy kích thước ảnh ban đầu
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream input = getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(input, null, options);
            input.close();

            // Tính toán tỷ lệ inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;

            // Load ảnh đã scale
            input = getContentResolver().openInputStream(uri);
            Bitmap scaledBitmap = BitmapFactory.decodeStream(input, null, options);
            input.close();

            return scaledBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tính toán tỷ lệ inSampleSize để nén ảnh
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            // Tăng tỷ lệ inSampleSize cho đến khi kích thước phù hợp
            while ((halfHeight / inSampleSize) >= reqHeight &&
                    (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }


    // Thêm các size cho từng màu cụ thể
    private void addSizeFields(String colorName) {
        // Tạo một tiêu đề cho màu mới để hiển thị
        TextView colorTitle = new TextView(this);
        colorTitle.setText("Color: " + colorName);
        colorTitle.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        colorTitle.setTextSize(18);
        colorTitle.setPadding(0, 16, 0, 8);
        stockContainer.addView(colorTitle);

        for (Size size : sizes) {
            View sizeColorView = getLayoutInflater().inflate(R.layout.size_color_stock_item, null);

            // Liên kết các view trong layout với dữ liệu
            TextView sizeTextView = sizeColorView.findViewById(R.id.txt_size);
            EditText stockEditText = sizeColorView.findViewById(R.id.edit_stock);

            sizeTextView.setText((int) size.getSize() + "");
            stockEditText.setText("0"); // Default quantity

            // Thêm view vào container chính
            stockContainer.addView(sizeColorView);

            // Lưu thông tin size và stock vào danh sách tạm thời
            stockList.add(new StockItem(sizeTextView, stockEditText));
        }
    }

    // Handle updating the shoe details
    private void updateShoe() {
        setStatus("Shoe is added successfully!");
        String name = shoeName.getText().toString();
        String price = priceEditText.getText().toString();
        String brand = brandSpinner.getSelectedItem().toString();
        String description = descriptionEditText.getText().toString();
        // Validate data
        if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all the required fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImages.isEmpty()) {
            Toast.makeText(this, "Please select at least one image before adding the product.", Toast.LENGTH_SHORT).show();
            return;  // Ngừng tiến trình nếu không có ảnh
        }
        // Handle stock update by size and color
        for (StockItem item : stockList) {
            String size = item.sizeEditText.getText().toString();
            String stock = item.stockEditText.getText().toString();

            // Validate size, color, stock
            if (size.isEmpty() || stock.isEmpty()) {
                Toast.makeText(this, "Please fill all size/stock fields.", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        AtomicInteger productId = new AtomicInteger(0);
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            if (appDatabase.productDao().checkProductExistbyName(name) == null) {
                long brandId = appDatabase.brandDao().getBrandByName(brand).getId();
                int x = appDatabase.productDao().lastProductId() + 1;
                productId.set(x);
                //add product
                Product product = new Product(productId.get(), name, Double.parseDouble(price), brandId, description,selectedImages.get(0).toString());
                appDatabase.productDao().addProduct(product);

                // Lưu các ảnh đã chọn vào Internal Storage trong cùng một tác vụ
                for (Bitmap bitmap : selectedImages) {
                    saveImageToInternalStorage(bitmap, productId.get());
                }
                //set first image of product
                String srcFirstImage = appDatabase.imageShoeDao().getFirstImageByProductId(productId.get());
                product.setImageSrc(srcFirstImage);
                appDatabase.productDao().updateProduct(product);


                //add color of product
                for (String colorName : colorNames) {
                    Color color = new Color(colorName, productId.get());
                    appDatabase.colorDao().addColor(color);
                }
                List<Long> colorIds = appDatabase.colorDao().getColorIdByProductId(productId.get());


                //add quantity of each size of each color of product
                int sizeId = 0;
                int colorCount = 0;
                for (StockItem item : stockList) {
                    if (sizeId == 11) {
                        sizeId = 0;
                        colorCount++;
                    }
                    sizeId++;
                    String stock = item.stockEditText.getText().toString();
                    ProductQuantity productQuantity = new ProductQuantity(productId.get(), sizeId, colorIds.get(colorCount), Integer.parseInt(stock));
                    appDatabase.productQuantityDAO().addProductQuantity(productQuantity);
                }
                //add url of product images

            } else {
                setStatus("Name of shoe existed!");
            }
            runOnUiThread(() -> {
                Toast.makeText(this, getStatus(), Toast.LENGTH_SHORT).show();
            });
        });


        // Finish activity or go back to previous screen
        finish();
    }


    //ham luu vao InternalStorage
    private void saveImageToInternalStorage(Bitmap bitmap, int productId) {
        try {
            // Tạo thư mục riêng cho sản phẩm dựa trên ProductID
            File productDir = new File(getFilesDir(), "product_" + productId);
            if (!productDir.exists()) {
                productDir.mkdir();  // Tạo thư mục nếu chưa tồn tại
            }

            // Tạo tên file với timestamp để tránh trùng
            String fileName = "image_" + System.currentTimeMillis() + ".jpg";
            File file = new File(productDir, fileName);

            // Lưu ảnh vào file
            try (FileOutputStream fos = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);  // Lưu dưới dạng JPEG
            }

            // Tạo đối tượng ImageShoe với đường dẫn ảnh đã lưu
            ImageShoe imageShoe = new ImageShoe(file.getAbsolutePath(), productId);
            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                appDatabase.imageShoeDao().addImageShoe(imageShoe);
            });
            // Lưu vào database hoặc danh sách nào đó
            Log.d("ImageSave", "Image saved at: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
        }
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

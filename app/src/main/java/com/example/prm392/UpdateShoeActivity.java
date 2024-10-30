package com.example.prm392;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
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
import com.example.prm392.R;
import com.example.prm392.adapter.ImageAdapter;
import com.example.prm392.entity.Brand;
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
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class UpdateShoeActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView shoeImage;
    private EditText shoeName, priceEditText, descriptionEditText;
    private Spinner brandSpinner, colorSpinner;
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

    Product product;
    List<Size> sizes;
    List<String> brands;
    private AppDatabase appDatabase;
    private List<ProductQuantity> productQuantities = new ArrayList<>();
    //Get Product id
    int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shoe);

        Intent intentGetId = getIntent();
        if(intentGetId!=null){
            productId = (int)intentGetId.getLongExtra("productId",-1);
        }


        // Initialize UI elements
        shoeName = findViewById(R.id.edit_shoe_name);
        priceEditText = findViewById(R.id.edit_price);
        brandSpinner = findViewById(R.id.spinner_brand);
        stockContainer = findViewById(R.id.stock_container);
        updateShoeButton = findViewById(R.id.btn_update_shoe);
        changeImageButton = findViewById(R.id.btn_change_image);
        addColor = findViewById(R.id.btn_add_new_color);
        descriptionEditText = findViewById(R.id.edit_description_shoe);


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
            product = appDatabase.productDao().getProductById(productId);
            sizes = appDatabase.sizeDao().getAllSizes();
            brands = appDatabase.brandDao().getAllBrand();
            String brandOfProduct = appDatabase.brandDao().getBrandNameById(product.getBrandId());
            colors = appDatabase.colorDao().getColorsById(productId);
            productQuantities = appDatabase.productQuantityDAO().getProductQuantityById(productId);
            // Lấy đường dẫn ảnh từ cơ sở dữ liệu
            List<String> imagePaths = appDatabase.imageShoeDao().getImagesById(productId);

            // Chuyển đổi các đường dẫn thành Bitmap
            for (String path : imagePaths) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (bitmap != null) {
                    selectedImages.add(bitmap);  // Thêm vào danh sách bitmap
                }
            }
            runOnUiThread(() -> {
                //product name,price, description
                shoeName.setText(product.getProductName());
                priceEditText.setText(String.format(Locale.US, "%.0f", product.getPrice()));
                descriptionEditText.setText(product.getDescription());
                //brands
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, brands);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brandSpinner.setAdapter(spinnerAdapter);
                // Tìm vị trí của brandOfProduct trong danh sách brands
                int brandPosition = brands.indexOf(brandOfProduct);
                if (brandPosition != -1) {
                    // Đặt giá trị selected cho Spinner
                    brandSpinner.setSelection(brandPosition);
                }

                //RecyclerView ảnh sp
                imageAdapter.notifyDataSetChanged();

                //Stock
                List<Integer> quantityByColor;
                for (Color color : colors) {
                    quantityByColor = new ArrayList<>();

                    // Lọc số lượng cho từng size của màu hiện tại
                    for (ProductQuantity pq : productQuantities) {

                        if (pq.getColorId() == color.getId()) {
                            quantityByColor.add(pq.getQuantity());
                        }
                    }
                    addSizeFields(color.getColor(), quantityByColor);
                }
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
                addSizeFields(newColorName, null);
                Toast.makeText(this, "New color added!", Toast.LENGTH_SHORT).show();
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
    private void addSizeFields(String colorName, List<Integer> quantityByColor) {
        colorNames.add(colorName);
        // Tạo một LinearLayout để chứa TextView và Button
        LinearLayout colorLayout = new LinearLayout(this);
        colorLayout.setOrientation(LinearLayout.VERTICAL); // Đặt chiều dọc cho layout
        colorLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout headLayout = new LinearLayout(this);
        headLayout.setOrientation(LinearLayout.HORIZONTAL); // Đặt chiều dọc cho layout
        headLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        // Tạo một tiêu đề cho màu mới để hiển thị
        TextView colorTitle = new TextView(this);
        colorTitle.setText("Color: " + colorName);
        colorTitle.setLayoutParams(new LinearLayout.LayoutParams(
                0, // Chiều rộng là 0 để sử dụng layout_weight
                LinearLayout.LayoutParams.WRAP_CONTENT,
                4 // Tỉ lệ chiều rộng cho phép TextView chiếm không gian còn lại
        ));
        colorTitle.setTextSize(18);
        colorTitle.setPadding(0, 16, 0, 8);


        // Tạo Button xóa
        Button deleteButton = new Button(this);
        deleteButton.setText("Remove"); // Hoặc bạn có thể sử dụng biểu tượng
        deleteButton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, // Chiều rộng sẽ tự động điều chỉnh
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1 // Tỉ lệ chiều rộng cho phép Button chiếm không gian
        ));

        // Thêm sự kiện cho nút xóa
        deleteButton.setOnClickListener(v -> {
            // Tạo một AlertDialog
            new AlertDialog.Builder(this)
                    .setTitle("ARE YOU SURE?")
                    .setMessage("Do you really want to delete this color?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                            Executor executor = Executors.newSingleThreadExecutor();
                            executor.execute(() -> {
                                appDatabase.colorDao().deleteColorByColorName(colorName);
                            });
                            // Xóa màu và tất cả các size liên quan
                            stockContainer.removeView(colorLayout); // Xóa toàn bộ colorLayout
                            colorNames.remove(colorName);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss(); // Đóng hộp thoại nếu chọn "No"
                    })
                    .show(); // Hiển thị hộp thoại
        });

        // Thêm TextView và Button vào LinearLayout
        headLayout.addView(colorTitle);
        headLayout.addView(deleteButton);
        colorLayout.addView(headLayout);


        int count = 0;
        for (Size size : sizes) {

            View sizeColorView = getLayoutInflater().inflate(R.layout.size_color_stock_item, null);

            // Liên kết các view trong layout với dữ liệu
            TextView sizeTextView = sizeColorView.findViewById(R.id.txt_size);
            EditText stockEditText = sizeColorView.findViewById(R.id.edit_stock);

            sizeTextView.setText((int) size.getSize() + "");
            stockEditText.setText("0"); // Default quantity

            if (quantityByColor != null) {
                stockEditText.setText(quantityByColor.get(count).toString());
                count++;
            }

            // Thêm view vào colorLayout (thay vì stockContainer)
            colorLayout.addView(sizeColorView);

            // Lưu thông tin size và stock vào danh sách tạm thời
            stockList.add(new UpdateShoeActivity.StockItem(sizeTextView, stockEditText));
        }
        // Thêm LinearLayout vào stockContainer
        stockContainer.addView(colorLayout);
    }











    // Handle updating the shoe details
    private void updateShoe() {
        String name = shoeName.getText().toString();
        String price = priceEditText.getText().toString();
        String brandName = brandSpinner.getSelectedItem().toString();
        String description = descriptionEditText.getText().toString();
        // Validate data
        if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all the required fields.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedImages.isEmpty()) {
            Toast.makeText(this, "Please select at least one image before updateting the product.", Toast.LENGTH_SHORT).show();
            return;  // Ngừng tiến trình nếu không có ảnh
        }
        // Handle stock update by size and color
        for (UpdateShoeActivity.StockItem item : stockList) {
            String size = item.sizeEditText.getText().toString();
            String stock = item.stockEditText.getText().toString();

            // Validate size, color, stock
            if (size.isEmpty() || stock.isEmpty()) {
                Toast.makeText(this, "Please fill all size/stock fields.", Toast.LENGTH_SHORT).show();
                return;
            }
        }


        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Product productToUpdate = new Product();
            productToUpdate.setId(productId);
            productToUpdate.setProductName(name);
            productToUpdate.setPrice(Double.parseDouble(price));
            productToUpdate.setDescription(description);
            Brand brand = appDatabase.brandDao().getBrandByName(brandName);
            productToUpdate.setBrandId(brand.getId());

            // Xoa thu muc chua anh cu
            File productDir = new File(getFilesDir(), "product_" + productId);
            // Xóa tất cả các tệp trong thư mục
            for (File file : productDir.listFiles()) {
                if (file.isFile()) {
                    file.delete();  // Xóa từng tệp
                }
            }
            // Xóa thư mục sau khi đã xóa hết tệp
            productDir.delete();
            //xoa anh cu
            appDatabase.imageShoeDao().deleteImagesByProductId(productId);;

            // Lưu các ảnh đã chọn vào Internal Storage trong cùng một tác vụ
            for (Bitmap bitmap : selectedImages) {
                saveImageToInternalStorage(bitmap, productId);
            }
            //set first image of product
            String srcFirstImage = appDatabase.imageShoeDao().getFirstImageByProductId(productId);
            productToUpdate.setImageSrc(srcFirstImage);
            appDatabase.productDao().updateProduct(productToUpdate);


            //xoa các mau va quantity cua san pham do
            appDatabase.colorDao().deleteColorByProductId(productId);;
            //add color of product
            for (String colorName : colorNames) {
                Color color = new Color(colorName, productId);
                appDatabase.colorDao().addColor(color);
            }
            List<Long> colorIds = appDatabase.colorDao().getColorIdByProductId(productId);


            //add quantity of each size of each color of product
            int sizeId = 0;
            int colorCount = 0;
            for (UpdateShoeActivity.StockItem item : stockList) {
                if (sizeId == 11) {
                    sizeId = 0;
                    colorCount++;
                }
                sizeId++;
                String stock = item.stockEditText.getText().toString();
                ProductQuantity productQuantity = new ProductQuantity(productId, sizeId, colorIds.get(colorCount), Integer.parseInt(stock));
                appDatabase.productQuantityDAO().addProductQuantity(productQuantity);
            }
            //add url of product images

            runOnUiThread(() -> {
                Toast.makeText(this, "Shoe updated successfully!", Toast.LENGTH_SHORT).show();
            });
        });


        // Finish activity or go back to previous screen
        finish();
    }

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

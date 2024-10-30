package com.example.prm392;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm392.Data.AppDatabase;
import com.example.prm392.adapter.ColorListAdapter;
import com.example.prm392.adapter.ImagePagerAdapter;
import com.example.prm392.adapter.SizeListAdapter;
import com.example.prm392.entity.Color;
import com.example.prm392.entity.Product;
import com.example.prm392.entity.ProductQuantity;
import com.example.prm392.entity.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DetailActivity extends AppCompatActivity {
    Product product;
    List<Size> sizes;
    List<Color> colors = new ArrayList<Color>();
    List<String> brands;
    private AppDatabase appDatabase;
    private List<ProductQuantity> productQuantities = new ArrayList<>();
    //Get Product id
    int productId;
    private SizeListAdapter sizeListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        appDatabase = AppDatabase.getAppDatabase(getApplicationContext());
        RecyclerView recyclerViewColor = findViewById(R.id.colorList);
        TextView productName = findViewById(R.id.txt_product_name);
        TextView description = findViewById(R.id.descriptionTxt);
        TextView price = findViewById(R.id.txt_price);
        RecyclerView recyclerViewSize = findViewById(R.id.sizeList);
        ViewPager2 images = findViewById(R.id.vp_images);

        Intent intentGetId = getIntent();
        if (intentGetId != null) {
            productId = (int) intentGetId.getLongExtra("productId", -1);

        }

        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            colors = appDatabase.colorDao().getColorsById(productId);
            product = appDatabase.productDao().getProductById(productId);
            sizes = appDatabase.sizeDao().getAllSizes();
            List<Bitmap> bitmapList = loadProductImages();  // Hàm này sẽ lấy danh sách ảnh
            //color list
            ColorListAdapter colorAdapter = new ColorListAdapter(getApplicationContext(),colors, selectedColor -> {
                // Khi màu được chọn, lọc danh sách size tương ứng từ kho
                getAvailableSizesForColor(selectedColor,productId);
            });
            runOnUiThread(() -> {
                productName.setText(product.getProductName());
                description.setText(product.getDescription());
                price.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));

                //color list
                recyclerViewColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                recyclerViewColor.setAdapter(colorAdapter);
                //size list
                sizeListAdapter = new SizeListAdapter(getApplicationContext(), sizes);
                recyclerViewSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                recyclerViewSize.setAdapter(sizeListAdapter);

                //image
                // Tạo adapter và gắn vào ViewPager2
                ImagePagerAdapter adapter = new ImagePagerAdapter(this, bitmapList);
                images.setAdapter(adapter);
            });
        });

        //Handle back button
        // Tìm ImageView với id backBtn
        ImageView backBtn = findViewById(R.id.backBtn);
        // Gán sự kiện OnClickListener
        backBtn.setOnClickListener(v -> {
            finish();
        });

    }

    private void getAvailableSizesForColor(int selectedColor, int productId) {
        RecyclerView recyclerViewSize = findViewById(R.id.sizeList);

        //xoa cac size default
        sizes.clear();
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Integer> sizeIds;
            sizeIds = appDatabase.productQuantityDAO().getProductQuantityByProductIdAndColorId(productId,selectedColor);
            for (int sizeId : sizeIds){
                sizes.add(appDatabase.sizeDao().getSizeBySizeId(sizeId));
            }
            runOnUiThread(()->{
                sizeListAdapter = new SizeListAdapter(getApplicationContext(), sizes);
                recyclerViewSize.setAdapter(sizeListAdapter); // Gán adapter cho RecyclerView
            });
        });


    }

    private List<Bitmap> loadProductImages() {
        List<Bitmap> bitmaps = new ArrayList<>();
        List<String> imagePaths = appDatabase.imageShoeDao().getImagesById(productId);

        // Chuyển đổi các đường dẫn thành Bitmap
        for (String path : imagePaths) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            if (bitmap != null) {
                bitmaps.add(bitmap);  // Thêm vào danh sách bitmap
            }
        }
        return bitmaps;
    }

}
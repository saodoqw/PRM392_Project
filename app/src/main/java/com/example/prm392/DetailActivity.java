package com.example.prm392;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm392.DAO.CartDAO;
import com.example.prm392.Data.AppDatabase;
import com.example.prm392.adapter.ColorListAdapter;
import com.example.prm392.adapter.ImagePagerAdapter;
import com.example.prm392.adapter.SizeListAdapter;
import com.example.prm392.entity.Cart;
import com.example.prm392.entity.Color;
import com.example.prm392.entity.DTO.ProductInCartWithQuantity;
import com.example.prm392.entity.Product;
import com.example.prm392.entity.ProductQuantity;
import com.example.prm392.entity.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DetailActivity extends AppCompatActivity {
    private int selectedColorId = 0;
    private int selectedSizeId = 0;
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
        ImageButton cartBtn = findViewById(R.id.cartBtn);
        cartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
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
            ColorListAdapter colorAdapter = new ColorListAdapter(getApplicationContext(), colors, selectedColor -> {
                // Lưu lại màu đã chọn
                this.selectedColorId = selectedColor;
                // Khi màu được chọn, lọc danh sách size tương ứng từ kho
                getAvailableSizesForColor(selectedColor, productId);
            });
            runOnUiThread(() -> {
                productName.setText(product.getProductName());
                description.setText(product.getDescription());
                price.setText(String.format(Locale.getDefault(), "$%.2f", product.getPrice()));

                //color list
                recyclerViewColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                recyclerViewColor.setAdapter(colorAdapter);
                //size list
                sizeListAdapter = new SizeListAdapter(getApplicationContext(), sizes, selectedSize -> {
                    // Lưu lại size đã chọn
                    this.selectedSizeId = selectedSize;
                });
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
        Button addToCartBtn = findViewById(R.id.addToCartBtn);
        // Khi người dùng nhấn nút "Buy Now"
        addToCartBtn.setOnClickListener(v -> {
            if (selectedColorId != 0 && selectedSizeId != 0) {
                Toast.makeText(this, "Color: " + selectedColorId + ", Size: " + selectedSizeId, Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("login", MODE_PRIVATE);
                int accountId = sharedPreferences.getInt("ACCOUNT_ID", -1);
                if (accountId == -1) {
                    return;
                }
                addProductToCart(appDatabase.cartDao(), productId, accountId, 1, selectedSizeId, selectedColorId);
            } else {
                Toast.makeText(this, "Please select both color and size!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void addProductToCart(CartDAO cartDAO, long productId, long accountId, int quantity, long sizeId, long colorId) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            int availableQuantity = cartDAO.getAvailableQuantity(productId, sizeId, colorId);
            if (availableQuantity >= quantity) {
                Cart newCart = new Cart(0, null, null, null, null, null, null, quantity, productId, accountId, colorId, sizeId);
                cartDAO.insertCart(newCart);
            } else {
                // Handle the case where the quantity is not sufficient
                System.out.println("Not enough quantity available");
            }
        });

    }

    private void getAvailableSizesForColor(int selectedColor, int productId) {
        RecyclerView recyclerViewSize = findViewById(R.id.sizeList);

        //xoa cac size default
        sizes.clear();
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Integer> sizeIds;
            sizeIds = appDatabase.productQuantityDAO().getProductQuantityByProductIdAndColorId(productId, selectedColor);
            for (int sizeId : sizeIds) {
                sizes.add(appDatabase.sizeDao().getSizeBySizeId(sizeId));
            }
            runOnUiThread(() -> {
                sizeListAdapter = new SizeListAdapter(getApplicationContext(), sizes, selectedSize -> {
                    // Lưu lại size đã chọn
                    this.selectedSizeId = selectedSize;
                });
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
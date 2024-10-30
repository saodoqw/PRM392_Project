package com.example.prm392.Data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.prm392.DAO.*;
import com.example.prm392.entity.*;
import com.example.prm392.entity.Enums.RoleName;

import java.util.concurrent.Executors;

@Database(entities = {About.class, Account.class, Brand.class, Cart.class,
        Color.class, Comment.class, Coupon.class, CouponType.class,
        Order.class, OrderDetail.class, Policy.class, Product.class, Role.class, Size.class
        , ImageShoe.class, ProductQuantity.class}
        , version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "PRM392ShoesStore";
    private static AppDatabase appDatabase;

    public static synchronized AppDatabase getAppDatabase(Context context) {
        if (appDatabase == null) {
            synchronized (AppDatabase.class) {
                if (appDatabase == null) {
                    appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class,
                                    DATABASE_NAME)
                            .fallbackToDestructiveMigration() // delete all data when version is changed
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Add default role for user
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        // Set default role for user
                                        Role roleAdmin = new Role(0, RoleName.ADMIN);
                                        Role roleUser = new Role(0, RoleName.USER);
                                        appDatabase.roleDao().insert(roleAdmin);
                                        appDatabase.roleDao().insert(roleUser);

                                        //Chèn dữ liệu mặc định cho các size
                                        for (int i = 35; i <= 45; i++) {
                                            Size size = new Size(0, i);
                                            Log.d("Database", "Inserted size: " + i); // In log để kiểm tra
                                            appDatabase.sizeDao().insert(size);
                                        }
                                        //Add default brand
                                        Brand brand = new Brand("Adidas");
                                        Brand brand1 = new Brand("Nike");
                                        appDatabase.brandDao().addBrand(brand);
                                        appDatabase.brandDao().addBrand(brand1);
                                        //Add default product
                                        Product product = new Product(0, "Adidas Ultra Boost", 200, 1, "Adidas Ultra Boost", "adidas_ultraboost.png");
                                        Product product1 = new Product(0, "Nike Air Max", 150, 2, "Nike Air Max", "nike_air_max.png");
                                        Product product2 = new Product(0, "Puma Rsx", 300, 1, "Puma Rsx", "puma_rsx.png");
                                        appDatabase.productDao().addProduct(product);
                                        appDatabase.productDao().addProduct(product1);
                                        appDatabase.productDao().addProduct(product2);
                                    });
                                }
                            })
                            .build();
                }
            }
        }
        return appDatabase;
    }

    //declare abstract method for each DAO
    public abstract AboutDAO aboutDao();

    public abstract AccountDAO accountDao();

    public abstract BrandDAO brandDao();

    public abstract CartDAO cartDao();

    public abstract ColorDAO colorDao();

    public abstract CommentDAO commentDao();

    public abstract CouponDAO couponDao();

    public abstract CouponTypeDAO couponTypeDao();

    public abstract OrderDAO orderDao();

    public abstract OrderDetailDAO orderDetailDao();

    public abstract PolicyDAO policyDao();

    public abstract ProductDAO productDao();

    public abstract RoleDAO roleDao();

    public abstract SizeDAO sizeDao();

    public abstract ImageShoeDAO imageShoeDao();

    public abstract ProductQuantityDAO productQuantityDAO();
}

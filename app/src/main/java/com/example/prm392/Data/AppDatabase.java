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

import java.sql.Date;
import java.util.Calendar;
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
                                    // Add default data
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
                                        Brand brand1 = new Brand("Nike");
                                        appDatabase.brandDao().addBrand(brand1);
                                        Brand brand2 = new Brand("Adidas");
                                        appDatabase.brandDao().addBrand(brand2);
                                        Brand brand3 = new Brand("Puma");
                                        appDatabase.brandDao().addBrand(brand3);
                                        Brand brand4 = new Brand("Vans");
                                        appDatabase.brandDao().addBrand(brand4);
                                        Brand brand5 = new Brand("Converse");
                                        appDatabase.brandDao().addBrand(brand5);
                                        Brand brand6 = new Brand("Reebok");
                                        appDatabase.brandDao().addBrand(brand6);
                                        Brand brand7 = new Brand("New Balance");
                                        appDatabase.brandDao().addBrand(brand7);
                                        Brand brand8 = new Brand("Gucci");
                                        appDatabase.brandDao().addBrand(brand8);
                                        Brand brand9 = new Brand("LV");
                                        appDatabase.brandDao().addBrand(brand9);
                                        //Coupon
                                        CouponType couponType1 = new CouponType(1,"NoCoupon");
                                        appDatabase.couponTypeDao().addCouponType(couponType1);
                                        CouponType couponType2 = new CouponType(2,"Discount");
                                        appDatabase.couponTypeDao().addCouponType(couponType2);
                                        Coupon coupon1 = new Coupon(1, (System.currentTimeMillis()),  (System.currentTimeMillis()), null,"admin",
                                                "admin", null, "",  1,0,0, 0,  new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 86400000L), 0, 0,  true );

                                        Coupon coupon2 = new Coupon(2, (System.currentTimeMillis()),  (System.currentTimeMillis()), null,"admin",
                                                "admin", null, "SAVE10",  2,10,100, 1000,  new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis() + 86400000L), 0, 0,  true );

                                        appDatabase.couponDao().addCoupon(coupon1);
                                        appDatabase.couponDao().addCoupon(coupon2);

                                        //Default admin account
                                        Account account = new Account(0, System.currentTimeMillis(),null,
                                                null,null,null,null,
                                                "admin","admin","123456789","Address",null,1);
                                        appDatabase.accountDao().insert(account);

                                        // Policy
                                        Policy policy1 = new Policy(0, null, null, null, null, null, null, "Yêu cầu đổi mật khẩu mỗi 90 ngày.", 1);
                                        Policy policy2 = new Policy(0, null, null, null, null, null, null, "Chỉ quản trị viên mới có quyền hủy đơn hàng sau khi đã xác nhận.", 1);
                                        appDatabase.policyDao().insert(policy1);
                                        appDatabase.policyDao().insert(policy2);
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

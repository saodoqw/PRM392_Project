package com.example.prm392.Data;

import android.content.Context;

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
        Order.class, OrderDetail.class, Policy.class, Product.class, Role.class, Size.class}
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
                            .fallbackToDestructiveMigration() // Tùy chọn nếu bạn muốn xóa dữ liệu khi có thay đổi schema
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    // Thêm dữ liệu mặc định khi database được tạo lần đầu tiên
                                    Executors.newSingleThreadExecutor().execute(() -> {
                                        // Chèn dữ liệu mặc định cho các role
                                        Role roleAdmin = new Role(0, RoleName.ADMIN);
                                        Role roleUser = new Role(0, RoleName.USER);
                                        appDatabase.roleDao().insert(roleAdmin);
                                        appDatabase.roleDao().insert(roleUser);
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
}

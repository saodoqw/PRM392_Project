package com.example.prm392.Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.prm392.DAO.*;
import com.example.prm392.entity.*;

@Database(entities = {About.class,Account.class, Brand.class, Cart.class,
        Color.class, Comment.class, Coupon.class, CouponType.class,
        Order.class, OrderDetail.class, Policy.class, Product.class, Role.class, Size.class}
        , version =1,exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
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

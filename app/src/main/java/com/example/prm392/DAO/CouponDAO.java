package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392.entity.Coupon;
import com.example.prm392.entity.CouponType;

import java.util.List;

@Dao
public interface CouponDAO {
    @Update
    void updateCoupon(Coupon coupon);

    @Query("SELECT * FROM coupon WHERE id = :couponId")
    Coupon getCouponById(long couponId);

    @Query("SELECT * FROM coupon")
    List<Coupon> getAllCoupons();

    @Insert
    void addCoupon(Coupon coupon2);
}

package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392.entity.Coupon;

import java.util.List;

@Dao
public interface CouponDAO {
    @Update
    void updateCoupon(Coupon coupon);

    @Query("SELECT * FROM coupon WHERE id = :couponId")
    Coupon getCouponById(long couponId);

    @Query("SELECT * FROM coupon")
    List<Coupon> getAllCoupons();
}

package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.prm392.entity.CouponType;

@Dao
public interface CouponTypeDAO {
    @Insert
    void addCouponType(CouponType couponType1);
}

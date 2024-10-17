package com.example.prm392.entity;

import androidx.room.Entity;

@Entity(tableName = "coupon_type")
public class CouponType extends BaseEntity{
    private String couponType;

    public CouponType(long id, String couponType) {
        super(id);
        this.couponType = couponType;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

}

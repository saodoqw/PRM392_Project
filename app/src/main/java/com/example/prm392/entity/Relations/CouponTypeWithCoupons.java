package com.example.prm392.entity.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.prm392.entity.Coupon;
import com.example.prm392.entity.CouponType;

import java.util.List;

public class CouponTypeWithCoupons {
    @Embedded
    private CouponType couponType;

    @Relation(
            parentColumn = "id",
            entityColumn = "coupon_type_id"
    )
    public List<Coupon> coupons;
}

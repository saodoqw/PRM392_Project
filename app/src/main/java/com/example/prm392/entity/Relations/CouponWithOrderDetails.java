package com.example.prm392.entity.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.prm392.entity.Coupon;
import com.example.prm392.entity.OrderDetail;

import java.util.List;

public class CouponWithOrderDetails {
    @Embedded
    private Coupon coupon;

    @Relation(
            parentColumn = "id",
            entityColumn = "coupon_id"
    )
    public List<OrderDetail> orderDetails;
}

package com.example.prm392.entity.Relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.prm392.entity.Order;
import com.example.prm392.entity.OrderDetail;

import java.util.List;

public class OrderWithOrderDetails {
    @Embedded
    private Order order;

    @Relation(
            parentColumn = "id",
            entityColumn = "order_id"
    )
    public List<OrderDetail> orderDetails;
}

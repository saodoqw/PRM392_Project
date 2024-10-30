package com.example.prm392.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.prm392.entity.Order;

import java.util.List;

@Dao
public interface OrderDAO {
    @Query("SELECT * FROM orders")
    LiveData<List<Order>> getAllOrders();

    @Query("SELECT * FROM orders WHERE id = :orderId")
    LiveData<Order> getOrderById(long orderId);

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    void updateOrderStatus(long orderId, String status);
}

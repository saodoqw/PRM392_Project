package com.example.prm392.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.prm392.entity.Order;

import java.util.List;
import androidx.room.Insert;

import com.example.prm392.entity.Order;
import com.example.prm392.entity.OrderDetail;

@Dao
public interface OrderDAO {
    @Query("SELECT * FROM orders")
    LiveData<List<Order>> getAllOrders();

    @Query("SELECT * FROM orders WHERE id = :orderId")
    LiveData<Order> getOrderById(long orderId);

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    void updateOrderStatus(long orderId, String status);

    @Insert
    long insertOrder(Order order);

    @Query("UPDATE orders SET shipping_address = :newAddress WHERE id = :orderId")
    void updateShippingAddress(long orderId, String newAddress);
}

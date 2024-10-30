package com.example.prm392.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.prm392.entity.OrderDetail;

import java.util.List;
import androidx.room.Insert;

import com.example.prm392.entity.OrderDetail;

@Dao
public interface OrderDetailDAO {

    @Query("SELECT * FROM order_detail")
    List<OrderDetail> getAllOrderDetail();

    @Query("SELECT * FROM order_detail WHERE order_id = :orderID")
    LiveData<List<OrderDetail>> getOrderDetailByOrderId(long orderID);

    @Insert
    void insertOrderDetail(OrderDetail orderDetail);
}

package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.prm392.entity.Order;
import com.example.prm392.entity.OrderDetail;

@Dao
public interface OrderDAO {
    @Insert
    long insertOrder(Order order);

}

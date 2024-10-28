package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.prm392.entity.OrderDetail;

@Dao
public interface OrderDetailDAO { @Insert
void insertOrderDetail(OrderDetail orderDetail);
}

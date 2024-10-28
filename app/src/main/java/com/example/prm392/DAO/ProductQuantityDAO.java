package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.prm392.entity.ProductQuantity;

@Dao
public interface ProductQuantityDAO {
    @Insert
    void addProductQuantity(ProductQuantity productQuantity);
}
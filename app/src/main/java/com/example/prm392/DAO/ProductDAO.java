package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.Product;

@Dao
public interface ProductDAO {
    @Query("SELECT id FROM product ORDER BY id DESC LIMIT 1")
    int lastProductId();
    @Insert
    void addProduct(Product product);
    @Query("SELECT * FROM product WHERE id = :x")
    Product checkProductExistbyId(int x);

    @Query("SELECT * FROM product WHERE productName = :name")
    Product checkProductExistbyName(String name);
}

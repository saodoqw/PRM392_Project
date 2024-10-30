package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392.entity.Product;

import java.util.List;

@Dao
public interface ProductDAO {
    @Query("SELECT id FROM product ORDER BY id DESC LIMIT 1")
    int lastProductId();

    @Insert
    void addProduct(Product product);

    @Query("SELECT * FROM product WHERE productName = :name")
    Product checkProductExistbyName(String name);

    @Query("SELECT * FROM product WHERE id = :i")
    Product getProductById(int i);

    @Update
    void updateProduct(Product product);

    @Query("SELECT * FROM product")
    List<Product> getAllProducts();
    @Delete
    void deleteProduct(Product product);
}

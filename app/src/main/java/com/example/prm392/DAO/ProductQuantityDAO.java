package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.ProductQuantity;

import java.util.List;

@Dao
public interface ProductQuantityDAO {
    @Insert
    void addProductQuantity(ProductQuantity productQuantity);

    @Query("SELECT * from product_quantity where productId = :productId")
    List<ProductQuantity> getProductQuantityById(int productId);
    @Query("SELECT sizeId from product_quantity where productId = :productId and colorId = :selectedColor and quantity != 0")
    List<Integer> getProductQuantityByProductIdAndColorId(int productId, int selectedColor);
    @Query("SELECT quantity FROM product_quantity WHERE productId = :productId AND sizeId = :sizeId AND colorId = :colorId LIMIT 1")
    int getQuantity(long productId, long sizeId, long colorId);
    @Query("UPDATE product_quantity SET quantity = :newQuantity WHERE productId = :productId AND sizeId = :sizeId AND colorId = :colorId")
    void updateQuantity(long productId, long sizeId, long colorId, int newQuantity);
}

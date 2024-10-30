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
}

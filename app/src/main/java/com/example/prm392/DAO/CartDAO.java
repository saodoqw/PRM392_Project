package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prm392.entity.Cart;
import com.example.prm392.entity.DTO.ProductInCartWithQuantity;

import java.util.List;

@Dao
public interface CartDAO {

    @Query("SELECT product.*, SUM(cart.quantity) as totalQuantity, SUM(cart.quantity * product.price) as totalPrice " +
            "FROM product INNER JOIN cart ON product.id = cart.product_id " +
            "WHERE cart.account_id = :accountId " +
            "GROUP BY product.id")
    List<ProductInCartWithQuantity> getProductsInCartGroupedByAccountId(long accountId);

    @Query("DELETE FROM cart WHERE account_id = :accountId")
    void deleteCartsByAccountId(long accountId);
    @Insert
    void insertCart(Cart cart);
    @Update
    void updateCart(Cart cart);

    @Query("SELECT * FROM cart WHERE product_id = :productId LIMIT 1")
    Cart getCartItemByProductId(long productId);
    @Query("DELETE FROM cart WHERE account_id = :accountId")
    void deleteCartByAccountId(int accountId);
    @Delete
    void deleteCart(Cart cart);

}

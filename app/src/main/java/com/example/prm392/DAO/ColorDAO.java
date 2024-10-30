package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.Color;

import java.util.List;

@Dao
public interface ColorDAO {
    @Insert
    void addColor(Color color);
    @Query("SELECT id FROM Color where productId = :i")
    List<Long> getColorIdByProductId(int i);
    @Query("SELECT * FROM Color where productId = :i")
    List<Color> getColorsById(int i);
    @Query("DELETE FROM Color WHERE productId = :productId")
    void deleteColorByProductId(int productId);
    @Query("DELETE FROM Color WHERE color = :colorName")
    void deleteColorByColorName(String colorName);
    @Query("SELECT * FROM Color WHERE id = :id")
    Color getColorById(long id);
}

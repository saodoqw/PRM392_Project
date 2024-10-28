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
}

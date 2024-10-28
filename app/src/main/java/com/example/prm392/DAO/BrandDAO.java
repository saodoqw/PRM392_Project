package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.prm392.entity.Brand;

@Dao
public interface BrandDAO {
    @Query("SELECT * FROM brand WHERE brand_name = :brand")
    Brand getBrandByName(String brand);
}

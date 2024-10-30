package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.Brand;

import java.util.List;

@Dao
public interface BrandDAO {
    @Query("SELECT * FROM brand WHERE brand_name = :brand")
    Brand getBrandByName(String brand);
    @Insert
    void addBrand(Brand brand);
    @Query("SELECT brand_name FROM brand")
    List<String> getAllBrand();
    @Query("SELECT brand_name FROM brand WHERE id = :brandId")
    String getBrandNameById(long brandId);
    @Query("SELECT * FROM Brand")
    List<Brand> getAll();
}

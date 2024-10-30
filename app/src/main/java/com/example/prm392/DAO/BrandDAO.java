package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.Brand;

import java.util.List;

@Dao
public interface BrandDAO {
    @Query("SELECT * FROM Brand")
    List<Brand> getAll();

    @Insert
    void insert(Brand brand1);
}

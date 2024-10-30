package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.Role;
import com.example.prm392.entity.Size;

import java.util.List;

@Dao
public interface SizeDAO {
    @Insert
    void insert(Size size);

    @Query("SELECT * FROM Size")
    List<Size> getAllSizes();
}

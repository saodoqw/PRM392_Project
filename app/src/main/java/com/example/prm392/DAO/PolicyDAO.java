package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.Policy;

import java.util.List;

@Dao
public interface PolicyDAO {

    @Query("SELECT * FROM policy")
    List<Policy> getAllPolicy();

    @Insert
    void insert(Policy policy);
}

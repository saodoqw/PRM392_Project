package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.prm392.entity.Relations.RoleWithUsers;
import com.example.prm392.entity.Role;

@Dao
public interface RoleDAO {
    @Insert
    void insert(Role roleAdmin);
}

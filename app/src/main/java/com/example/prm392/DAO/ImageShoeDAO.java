package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.prm392.entity.ImageShoe;

@Dao
public interface ImageShoeDAO {
    @Insert
    void addImageShoe(ImageShoe imageShoe);
}

package com.example.prm392.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prm392.entity.ImageShoe;

import java.util.List;

@Dao
public interface ImageShoeDAO {
    @Insert
    void addImageShoe(ImageShoe imageShoe);
    @Query("SELECT imageSrc FROM ImageShoe WHERE productId = :i")
    List<String> getImagesById(int i);
}

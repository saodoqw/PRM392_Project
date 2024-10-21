package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.sql.Date;

@Entity(tableName = "about")
public class About extends BaseEntity{
    @ColumnInfo(name = "content", typeAffinity = ColumnInfo.TEXT)
    private String content;
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.TEXT)
    private String image;

    public About(long id, Date createdAt, Date updatedAt, Date deletedAt, String createdBy, String updatedBy, String deletedBy, String content, String image) {
        super(id, createdAt, updatedAt, deletedAt, createdBy, updatedBy, deletedBy);
        this.content = content;
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}

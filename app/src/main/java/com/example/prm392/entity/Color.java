package com.example.prm392.entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import java.sql.Date;

@Entity(tableName = "color")
public class Color extends BaseEntity{
    @ColumnInfo(name = "color", typeAffinity = ColumnInfo.TEXT)
    private String color;

    public Color(long id, Date createdAt, Date updatedAt, Date deletedAt, String createdBy, String updatedBy, String deletedBy, String color) {
        super(id, createdAt, updatedAt, deletedAt, createdBy, updatedBy, deletedBy);
        this.color = color;
    }
    @Ignore // Room sẽ bỏ qua constructor này
    public Color(long id,String color) {
        super(id);
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

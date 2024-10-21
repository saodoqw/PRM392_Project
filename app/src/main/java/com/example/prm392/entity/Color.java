package com.example.prm392.entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.sql.Date;

@Entity(tableName = "color")
public class Color extends BaseEntity{
    @ColumnInfo(name = "color", typeAffinity = ColumnInfo.INTEGER)
    private int color;

    public Color(long id, Date createdAt, Date updatedAt, Date deletedAt, String createdBy, String updatedBy, String deletedBy, int color) {
        super(id, createdAt, updatedAt, deletedAt, createdBy, updatedBy, deletedBy);
        this.color = color;
    }
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

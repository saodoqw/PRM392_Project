package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "size")
public class Size extends BaseEntity{
    @ColumnInfo(name = "size", typeAffinity = ColumnInfo.REAL)
    private float size;

    public Size(long id, float size) {
        super(id);
        this.size = size;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }
}

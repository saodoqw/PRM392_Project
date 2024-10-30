package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.sql.Date;

@Entity(tableName = "brand")
public class Brand extends BaseEntity{
    @ColumnInfo(name = "brand_name", typeAffinity = ColumnInfo.TEXT)
    private String brandName;

    public Brand(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}

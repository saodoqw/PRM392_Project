package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "shoe")
public class Shoe extends BaseEntity{
    @ColumnInfo(name = "shoe_name", typeAffinity = ColumnInfo.TEXT)
    private String name;
    @ColumnInfo(name = "price", typeAffinity = ColumnInfo.REAL) // REAL dùng cho số thập phân
    private double price;
    @ColumnInfo(name = "price", typeAffinity = ColumnInfo.INTEGER)
    private int imageResource;
    @ColumnInfo(name = "price", typeAffinity = ColumnInfo.TEXT)
    private String brand;  // New property for brand

    @Ignore // Room sẽ bỏ qua constructor này
    public Shoe(int id,String name, double price, int imageResource, String brand) {
        super(id);
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getBrand() {
        return brand;
    }
}


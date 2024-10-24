package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "product")
public class Product extends BaseEntity{
    @ColumnInfo(name = "product_name", typeAffinity = ColumnInfo.TEXT)
    private String productName;
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.TEXT)
    private String image;
    @ColumnInfo(name = "price", typeAffinity = ColumnInfo.TEXT)
    private double price;
    @ColumnInfo(name = "brand_id", typeAffinity = ColumnInfo.TEXT)
    private long brandId;

    public Product(long id, String productName, String image, double price, long brandId) {
        super(id);
        this.productName = productName;
        this.image = image;
        this.price = price;
        this.brandId = brandId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
    }
}

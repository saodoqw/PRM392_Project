package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;


@Entity(
        tableName = "product",
        foreignKeys = @ForeignKey(
                entity = Brand.class,                // Bảng cha (Brand)
                parentColumns = "id",           // Cột khóa chính trong bảng Brand
                childColumns = "brandId",            // Cột khóa ngoại trong bảng Product
                onDelete = ForeignKey.CASCADE        // Xóa sản phẩm nếu brand bị xóa
        )
)
public class Product extends BaseEntity{
    @ColumnInfo(name = "productName")
    private String productName;

    @ColumnInfo(name = "price")
    private double price;
    @ColumnInfo(index = true) //just add index = true
    private long brandId;

    private String description;

    public Product(long id, String productName
            , double price, long brandId, String description) {
        super(id);
        this.productName = productName;
        this.price = price;
        this.brandId = brandId;
        this.description = description;
    }
    public Product() {
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

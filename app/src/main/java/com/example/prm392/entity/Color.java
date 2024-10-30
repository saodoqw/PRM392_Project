package com.example.prm392.entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;

import java.sql.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "color",
        foreignKeys = @ForeignKey(
                entity = Product.class,
                parentColumns = "id",
                childColumns = "productId",
                onDelete = ForeignKey.CASCADE
        )
)
public class Color extends BaseEntity{
    @ColumnInfo(name = "color", typeAffinity = ColumnInfo.TEXT)
    private String color;
    @ColumnInfo(index = true) //just add index = true
    private long productId;

//    public Color(long id, String color, long productId) {
//        super(id);
//        this.color = color;
//        this.productId = productId;
//    }

    public Color(String color, long productId) {
        super();
        this.color = color;
        this.productId = productId;
    }
    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}




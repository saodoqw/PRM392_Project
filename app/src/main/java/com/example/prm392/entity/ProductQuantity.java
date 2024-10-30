package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.sql.Date;

@Entity(
        tableName = "product_quantity",
        foreignKeys = {
                @ForeignKey(entity = Product.class, parentColumns = "id", childColumns = "productId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Size.class, parentColumns = "id", childColumns = "sizeId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Color.class, parentColumns = "id", childColumns = "colorId", onDelete = ForeignKey.CASCADE)
        }
)
public class ProductQuantity extends BaseEntity{
    @ColumnInfo(index = true) //just add index = true
    private long productId;
    @ColumnInfo(index = true) //just add index = true
    private long sizeId;
    @ColumnInfo(index = true) //just add index = true
    private long colorId;
    private int quantity;

//    public ProductQuantity(long id, long productId, int quantity, long sizeId, long colorId) {
//        super(id);
//        this.productId = productId;
//        this.quantity = quantity;
//        this.sizeId = sizeId;
//        this.colorId = colorId;
//    }

    public ProductQuantity(long productId, long sizeId, long colorId, int quantity) {
        this.productId = productId;
        this.sizeId = sizeId;
        this.colorId = colorId;
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getColorId() {
        return colorId;
    }

    public void setColorId(long colorId) {
        this.colorId = colorId;
    }

    public long getSizeId() {
        return sizeId;
    }

    public void setSizeId(long sizeId) {
        this.sizeId = sizeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

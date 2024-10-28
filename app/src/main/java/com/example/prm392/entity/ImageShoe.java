package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ForeignKey;

import java.sql.Date;

@Entity(
        tableName = "imageShoe",
        foreignKeys = @ForeignKey(
                entity = Product.class,
                parentColumns = "id",
                childColumns = "productId",
                onDelete = ForeignKey.CASCADE
        )
)
public class ImageShoe extends BaseEntity{
    private int imageSrc;
    @ColumnInfo(index = true) //just add index = true
    private long productId;

    public ImageShoe(long id, Date createdAt, Date updatedAt, Date deletedAt, String createdBy, String updatedBy, String deletedBy, int imageSrc, long productId) {
        super(id, createdAt, updatedAt, deletedAt, createdBy, updatedBy, deletedBy);
        this.imageSrc = imageSrc;
        this.productId = productId;
    }

    public ImageShoe(long id, int image, long productId) {
        super(id);
        this.imageSrc = image;
        this.productId = productId;
    }

    public int getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(int imageSrc) {
        this.imageSrc = imageSrc;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}

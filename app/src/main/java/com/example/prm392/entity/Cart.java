package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.sql.Date;

@Entity(tableName = "cart",
        foreignKeys = {
                @ForeignKey(
                        entity = Account.class,
                        parentColumns = "id",
                        childColumns = "account_id",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(entity = Product.class,
                        parentColumns = "id",
                        childColumns = "product_id",
                        onDelete = ForeignKey.CASCADE
                )
        })
public class Cart extends BaseEntity {
    @ColumnInfo(name = "quantity", typeAffinity = ColumnInfo.INTEGER)
    private int quantity;
    @ColumnInfo(name = "product_id", index = true)
    private long productId;
    @ColumnInfo(name = "account_id", index = true)
    private long accountId;
    private long color;
    private long size;

    public Cart(long id, Date createdAt, Date updatedAt, Date deletedAt, String createdBy, String updatedBy, String deletedBy, int quantity, long productId, long accountId, long color, long size) {
        super(id, createdAt, updatedAt, deletedAt, createdBy, updatedBy, deletedBy);
        this.quantity = quantity;
        this.productId = productId;
        this.accountId = accountId;
        this.color = color;
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public void setColor(long color) {
        this.color = color;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
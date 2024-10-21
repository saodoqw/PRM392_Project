package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.sql.Date;

@Entity(tableName = "comment",
        foreignKeys = {
                @androidx.room.ForeignKey(
                        entity = Account.class,
                        parentColumns = "id",
                        childColumns = "account_id",
                        onDelete = androidx.room.ForeignKey.CASCADE
                ),
                @androidx.room.ForeignKey(
                        entity = Product.class,
                        parentColumns = "id",
                        childColumns = "product_id",
                        onDelete = androidx.room.ForeignKey.CASCADE
                )
        })
public class Comment extends BaseEntity{
    @ColumnInfo(name = "content", typeAffinity = ColumnInfo.TEXT)
    private String content;
    @ColumnInfo(name = "product_id", index = true)
    private long productId;
    @ColumnInfo(name = "account_id", index = true)
    private long accountId;

    public Comment(long id, Date createdAt, Date updatedAt, Date deletedAt, String createdBy, String updatedBy, String deletedBy, String content, long productId, long accountId) {
        super(id, createdAt, updatedAt, deletedAt, createdBy, updatedBy, deletedBy);
        this.content = content;
        this.productId = productId;
        this.accountId = accountId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

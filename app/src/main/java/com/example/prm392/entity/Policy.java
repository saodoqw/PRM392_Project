package com.example.prm392.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.sql.Date;

@Entity(tableName = "policy",
        foreignKeys = {
                @androidx.room.ForeignKey(
                        entity = Account.class,
                        parentColumns = "id",
                        childColumns = "account_id",
                        onDelete = androidx.room.ForeignKey.CASCADE
                )
        })
public class Policy extends BaseEntity {
    @ColumnInfo(name = "content", typeAffinity = ColumnInfo.TEXT)
    private String content;
    @ColumnInfo(name = "account_id", index = true)
    private long accountId;

    public Policy(long id, Date createdAt, Date updatedAt, Date deletedAt, String createdBy, String updatedBy, String deletedBy, String content, long accountId) {
        super(id, createdAt, updatedAt, deletedAt, createdBy, updatedBy, deletedBy);
        this.content = content;
        this.accountId = accountId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}

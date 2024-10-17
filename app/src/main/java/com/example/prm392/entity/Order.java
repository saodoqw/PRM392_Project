package com.example.prm392.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.sql.Date;

@Entity(tableName = "orders",
        foreignKeys = {
                @ForeignKey(
                        entity = Account.class,
                        parentColumns = "id",
                        childColumns = "account_id",
                        onDelete = ForeignKey.CASCADE
                )
        })
public class Order extends BaseEntity {
    @ColumnInfo(name = "order_date", defaultValue = "CURRENT_TIMESTAMP")
    private Date orderDate;
    @ColumnInfo(name = "total_amount", typeAffinity = ColumnInfo.INTEGER)
    private int totalAmount;
    @ColumnInfo(name = "status", typeAffinity = ColumnInfo.TEXT)
    private String status;
    @ColumnInfo(name = "invoice", typeAffinity = ColumnInfo.INTEGER)
    private int invoice;
    @ColumnInfo(name = "account_id", index = true)
    private int accountId;

    public Order(long id, Date createdAt, Date updatedAt, Date deletedAt, String createdBy, String updatedBy, String deletedBy, Date orderDate, int totalAmount, String status, int invoice, int accountId) {
        super(id, createdAt, updatedAt, deletedAt, createdBy, updatedBy, deletedBy);
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.invoice = invoice;
        this.accountId = accountId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getInvoice() {
        return invoice;
    }

    public void setInvoice(int invoice) {
        this.invoice = invoice;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}

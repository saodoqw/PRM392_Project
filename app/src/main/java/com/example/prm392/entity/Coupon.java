package com.example.prm392.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.sql.Date;

@Entity(tableName = "coupon",
        foreignKeys = {
                @ForeignKey(entity = CouponType.class,
                        parentColumns = "id",
                        childColumns = "discount_id",
                        onDelete = CASCADE)
        }
)
public class Coupon extends BaseEntity{
    @ColumnInfo(name = "coupon_code", typeAffinity = ColumnInfo.TEXT)
    private String couponCode;
    @ColumnInfo(name = "discount_id", index = true)
    private long discountId;
    @ColumnInfo(name = "discount_value", typeAffinity = ColumnInfo.INTEGER)
    private int discountValue;
    @ColumnInfo(name = "min_order_value", typeAffinity = ColumnInfo.INTEGER)
    private int minOrderValue;
    @ColumnInfo(name = "max_order_value", typeAffinity = ColumnInfo.INTEGER)
    private int maxOrderValue;
    @ColumnInfo(name = "start_date")
    private Date startDate;
    @ColumnInfo(name = "end_date")
    private Date endDate;
    @ColumnInfo(name = "usage_limit", typeAffinity = ColumnInfo.INTEGER)
    private int usageLimit;
    @ColumnInfo(name = "usage_count", typeAffinity = ColumnInfo.INTEGER)
    private int usageCount;
    @ColumnInfo(name = "status", typeAffinity = ColumnInfo.INTEGER)
    private boolean status;

    public Coupon(long id, Date createdAt, Date updatedAt, Date deletedAt, String createdBy, String updatedBy, String deletedBy, String couponCode, long discountId, int discountValue, int minOrderValue, int maxOrderValue, Date startDate, Date endDate, int usageLimit, int usageCount, boolean status) {
        super(id, createdAt, updatedAt, deletedAt, createdBy, updatedBy, deletedBy);
        this.couponCode = couponCode;
        this.discountId = discountId;
        this.discountValue = discountValue;
        this.minOrderValue = minOrderValue;
        this.maxOrderValue = maxOrderValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
        this.usageCount = usageCount;
        this.status = status;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(long discountId) {
        this.discountId = discountId;
    }

    public int getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }

    public int getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(int minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public int getMaxOrderValue() {
        return maxOrderValue;
    }

    public void setMaxOrderValue(int maxOrderValue) {
        this.maxOrderValue = maxOrderValue;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

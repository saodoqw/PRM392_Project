package com.example.prm392.entity;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.sql.Date;

@Entity(tableName = "order_detail",
foreignKeys = {
        @ForeignKey(entity = Order.class,
                parentColumns = "id",
                childColumns = "order_id",
                onDelete = CASCADE),
        @ForeignKey(entity = Product.class,
                parentColumns = "id",
                childColumns = "product_id",
                onDelete = CASCADE),
        @ForeignKey(entity = Coupon.class,
                parentColumns = "id",
                childColumns = "coupon_id",
                onDelete = CASCADE)
})
public class OrderDetail extends BaseEntity{
    @ColumnInfo(name = "quantity", typeAffinity = ColumnInfo.INTEGER)
    private int quantity;
    @ColumnInfo(name = "unit_price", typeAffinity = ColumnInfo.INTEGER)
    private int unitPrice;
    @ColumnInfo(name = "order_id", index = true)
    private long orderId;
    @ColumnInfo(name = "product_id", index = true)
    private long productId;
    @ColumnInfo(name = "size_id", index = true)
    private long sizeId;
    @ColumnInfo(name = "color_id", index = true)
    private long colorId;
    @ColumnInfo(name = "coupon_id", index = true)
    private long couponId;

    public OrderDetail(long id, Long createdAt, Long updatedAt, Long deletedAt, String createdBy, String updatedBy, String deletedBy, int quantity, int unitPrice, long orderId, long productId, long couponId, long sizeId, long colorId) {
        super(id, createdAt, updatedAt, deletedAt, createdBy, updatedBy, deletedBy);
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.orderId = orderId;
        this.productId = productId;
        this.couponId = couponId;
        this.sizeId = sizeId;
        this.colorId = colorId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public long getSizeId() { return sizeId; }

    public void setSizeId(long sizeId) { this.sizeId = sizeId; }

    public long getColorId() {
        return colorId;
    }

    public void setColorId(long colorId) {
        this.colorId = colorId;
    }
}

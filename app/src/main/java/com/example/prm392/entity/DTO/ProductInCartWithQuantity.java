package com.example.prm392.entity.DTO;

import androidx.room.Embedded;

import com.example.prm392.entity.Product;

public class ProductInCartWithQuantity {
    @Embedded
    public Product product;
    public int totalQuantity;
    public double totalPrice;
    public long color;
    public long size;

//    public String imageProductSrc;

//    public ProductInCartWithQuantity(Product product, int totalQuantity, double totalPrice, String imageProductSrc) {
//        this.product = product;
//        this.totalQuantity = totalQuantity;
//        this.totalPrice = totalPrice;
//        this.imageProductSrc = imageProductSrc;
//    }

    public ProductInCartWithQuantity(Product product, int totalQuantity, double totalPrice, long color, long size) {
        this.product = product;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.color = color;
        this.size = size;
    }

    public long getColor() {
        return color;
    }

    public void setColor(long color) {
        this.color = color;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

//    public String getImageProductSrc() {
//        return imageProductSrc;
//    }
//
//    public void setImageProductSrc(String imageProductSrc) {
//        this.imageProductSrc = imageProductSrc;
//    }
}
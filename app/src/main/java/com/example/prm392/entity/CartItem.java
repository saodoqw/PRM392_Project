package com.example.prm392.entity;

public class CartItem {
    private int id;
    private String name;
    private int quantity;
    private double price;

    public CartItem(int id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotalPrice() {
        return quantity * price;
    }
}
package com.example.prm392.entity;

public class Shoe {
    private String name;
    private double price;
    private int imageResource;
    private String brand;  // New property for brand

    public Shoe(String name, double price, int imageResource, String brand) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getBrand() {
        return brand;
    }
}


package com.example.prm392.entity;

public class Color {
    private int ColorId;
    private String Color;

    public Color( int colorId, String color) {
        Color = color;
        ColorId = colorId;
    }

    public int getColorId() {
        return ColorId;
    }

    public void setColorId(int colorId) {
        ColorId = colorId;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }
}

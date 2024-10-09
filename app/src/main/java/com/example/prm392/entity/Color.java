package com.example.prm392.entity;

public class Color {
    private int ColorId;
    private int Color;

    public Color( int colorId, int color) {
        Color = color;
        ColorId = colorId;
    }

    public int getColorId() {
        return ColorId;
    }

    public void setColorId(int colorId) {
        ColorId = colorId;
    }

    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
        Color = color;
    }
}

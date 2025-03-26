package com.example.nike.Model;

import java.util.List;

public class ProductColor {
    private int imageResource;
    private String colorName;
    private String price;
    private List<String> sizes; // Danh sách size của mỗi màu

    public ProductColor(int imageResource, String colorName, String price, List<String> sizes) {
        this.imageResource = imageResource;
        this.colorName = colorName;
        this.price = price;
        this.sizes = sizes;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getColorName() {
        return colorName;
    }

    public String getPrice() {
        return price;
    }

    public List<String> getSizes() {
        return sizes;
    }
}



package com.example.nike.Model;
import java.util.List;

public class ProductAdmin {
    private String id;
    private String name;
    private String category;
    private double price;
    private List<String> sizes;
    private String imageUrl;
    private String description; // Thêm mô tả sản phẩm

    public ProductAdmin() {
        // Constructor mặc định
    }

    public ProductAdmin(String id, String name, String category, double price, List<String> sizes, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.sizes = sizes;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public List<String> getSizes() { return sizes; }
    public String getImageUrl() { return imageUrl; }
    public String getDescription() { return description; } // Getter mô tả

    public void setDescription(String description) { this.description = description; } // Setter mô tả
}

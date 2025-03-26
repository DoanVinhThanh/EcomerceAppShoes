package com.example.nike.Model;

public class Product {
    public Product(int imageResIdNewProduct, String nameNewProduct, int priceProduct, String sexProduct) {
        this.imageResIdNewProduct = imageResIdNewProduct;
        this.nameNewProduct = nameNewProduct;
        this.SexProduct = sexProduct;
        this.PriceProduct = priceProduct;
    }

    private int imageResIdNewProduct;
    private String nameNewProduct;

    private int PriceProduct;
    private String SexProduct;



    public int getImageResIdNewProduct() {
        return imageResIdNewProduct;
    }

    public String getNameNewProduct() {
        return nameNewProduct;
    }
    public int getPriceProduct() {
        return PriceProduct;
    }

    public String getSexProduct() {
        return SexProduct;
    }
}

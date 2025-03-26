package com.example.nike.Model;

import java.util.List;

public class ProductAdmin {
    private int idProduct;
    private String nameProduct;
    private String priceProduct;
    private List<String> sizeProduct;
    private int idCategory;
    private String imageUrlProduct;


    public ProductAdmin(){}

    public ProductAdmin(int idProduct, String nameProduct, String priceProduct, List<String> sizeProduct, int idCategory, String imageUrlProduct) {
        this.idProduct = idProduct;
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
        this.sizeProduct = sizeProduct;
        this.idCategory = idCategory;
        this.imageUrlProduct = imageUrlProduct;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getPriceProduct() {
        return priceProduct;
    }

    public void setPriceProduct(String priceProduct) {
        this.priceProduct = priceProduct;
    }

    public List<String> getSizeProduct() {
        return sizeProduct;
    }

    public void setSizeProduct(List<String> sizeProduct) {
        this.sizeProduct = sizeProduct;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getImageUrlProduct() { // 🆕 Getter cho image
        return imageUrlProduct;
    }

    public void setImageUrlProduct(String imageUrlProduct) { // 🆕 Setter cho image
        this.imageUrlProduct = imageUrlProduct;
    }
}


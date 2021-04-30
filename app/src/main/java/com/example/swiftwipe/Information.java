package com.example.swiftwipe;

public class Information {

    private String productName;
    private double productPrice;
    private String productSize;
    private String productImage;
    private String productid;

    public Information(String productName, double productPrice, String productSize, String productImage, String productid) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productSize = productSize;
        this.productImage = productImage;
        this.productid = productid;
    }

    public Information() {
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }
}

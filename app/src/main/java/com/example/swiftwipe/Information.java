package com.example.swiftwipe;

public class Information {

    public String productName;
    public int productPrice;
    public String productSize;
    public String productImage;
    public String productid;



    public Information(String productName, int productPrice, String productSize, String productImage, String productid) {
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

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
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

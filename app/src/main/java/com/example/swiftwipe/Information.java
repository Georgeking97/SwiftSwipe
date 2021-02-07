package com.example.swiftwipe;

public class Information {

    private String productname;
    private String productsize;
    private int productprice;

    public Information() {

    }

    public Information(String productname, String productsize, int productprice){
        this.productname = productname;
        this.productsize = productsize;
        this.productprice = productprice;
    }

    public int getProductprice() {
        return productprice;
    }

    public void setProductprice(int productprice) {
        this.productprice = productprice;
    }

    public String getProductsize() {
        return productsize;
    }

    public void setProductsize(String productsize) {
        this.productsize = productsize;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }
}

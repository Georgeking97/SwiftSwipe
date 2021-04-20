package com.example.swiftwipe;

public class orderInfo {
    public String transactionId;
    public String cost;
    public boolean returned;
    public boolean coupon;

    public orderInfo(String transactionId, String cost, boolean returned, boolean coupon) {
        this.transactionId = transactionId;
        this.cost = cost;
        this.returned = returned;
        this.coupon = coupon;
    }
}

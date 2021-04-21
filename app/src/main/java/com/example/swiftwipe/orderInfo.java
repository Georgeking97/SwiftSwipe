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

    public orderInfo() {
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public boolean isCoupon() {
        return coupon;
    }

    public void setCoupon(boolean coupon) {
        this.coupon = coupon;
    }
}

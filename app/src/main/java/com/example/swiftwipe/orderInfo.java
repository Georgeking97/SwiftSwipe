package com.example.swiftwipe;

public class orderInfo {
    public String transactionId;
    public String cost;
    public boolean returned;

    public orderInfo(String transactionId, String cost, boolean returned) {
        this.transactionId = transactionId;
        this.cost = cost;
        this.returned = returned;
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
}

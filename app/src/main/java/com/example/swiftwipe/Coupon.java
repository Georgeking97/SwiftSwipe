package com.example.swiftwipe;

public class Coupon {
    public String code;
    public int value;

    public Coupon(String code, int value, boolean used) {
        this.code = code;
        this.value = value;
    }

    public Coupon() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

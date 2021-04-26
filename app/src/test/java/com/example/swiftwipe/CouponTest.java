package com.example.swiftwipe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CouponTest {
    Coupon coupon = new Coupon("01", 1);

    @Test
    void getCode() {
        assertEquals("01", coupon.getCode());
    }

    @Test
    void setCode() {
        coupon.setCode("02");
        assertEquals("02", coupon.getCode());
    }

    @Test
    void getValue() {
        assertEquals(1, coupon.getValue());
    }

    @Test
    void setValue() {
        coupon.setValue(2);
        assertEquals(2, coupon.getValue());
    }
}
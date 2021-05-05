package com.example.swiftwipe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class orderInfoTest {
    orderInfo orderInfo = new orderInfo("01", "10", false, false);

    @Test
    void getTransactionId() {
        assertEquals("01", orderInfo.getTransactionId());
    }

    @Test
    void setTransactionId() {
        orderInfo.setTransactionId("02");
        assertEquals("02", orderInfo.getTransactionId());
    }

    @Test
    void getCost() {
        assertEquals("10", orderInfo.getCost());
    }

    @Test
    void setCost() {
        orderInfo.setCost("20");
        assertEquals("20", orderInfo.getCost());
    }

    @Test
    void isReturned() {
        assertEquals(false, orderInfo.isReturned());
    }

    @Test
    void setReturned() {
        orderInfo.setReturned(true);
        assertEquals(true, orderInfo.isReturned());
    }

    @Test
    void isCoupon() {
        assertEquals(false, orderInfo.isCoupon());
    }

    @Test
    void setCoupon() {
        orderInfo.setCoupon(true);
        assertEquals(true, orderInfo.isCoupon());
    }
}
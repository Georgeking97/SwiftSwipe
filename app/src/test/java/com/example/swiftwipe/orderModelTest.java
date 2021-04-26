package com.example.swiftwipe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class orderModelTest {
    orderModel orderModel = new orderModel("01");


    @Test
    void getKey() {
        assertEquals("01",orderModel.getKey());
    }

    @Test
    void setKey() {
        orderModel.setKey("02");
        assertEquals("02", orderModel.getKey());
    }
}
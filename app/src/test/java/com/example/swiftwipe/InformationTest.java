package com.example.swiftwipe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InformationTest {
    Information info = new Information("name", 10, "medium", "image", "01");

    @Test
    void getProductName() {
        assertEquals("name", info.getProductName());
    }

    @Test
    void getProductPrice() {
        assertEquals(10, info.getProductPrice());
    }

    @Test
    void getProductSize() {
        assertEquals("medium", info.getProductSize());
    }

    @Test
    void getProductImage() {
        assertEquals("image", info.getProductImage());
    }

    @Test
    void getProductid() {
        assertEquals("01", info.getProductid());
    }

    @Test
    void setProductName() {
        info.setProductName("test");
        assertEquals("test", info.getProductName());
    }

    @Test
    void setProductPrice() {
        info.setProductPrice(15);
        assertEquals(15, info.getProductPrice());
    }

    @Test
    void setProductSize() {
        info.setProductSize("small");
        assertEquals("small", info.getProductSize());
    }

    @Test
    void setProductImage() {
        info.setProductImage("test");
        assertEquals("test", info.getProductImage());
    }

    @Test
    void setProductid() {
        info.setProductid("02");
        assertEquals("02", info.getProductid());
    }
}
package com.claire.qanda;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SumTest {

    @BeforeEach
    void setUp() {
        System.out.println("this is before");
    }

    @AfterEach
    void tearDown() {
        System.out.println("this is after");
    }

    @Test
    void should_return_sum() {
        Sum a = new Sum ();
        int i = a.addNumber(2,5);
        assertEquals(7,i,"same number");
    }
}
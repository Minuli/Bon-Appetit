package com.example.bon_apetit;

import org.junit.Before;
import org.junit.Test;

public class TotalAmount {
    private mycart my_cart;

    @Before
    public  void setUp(){
        my_cart = new mycart();
    }

    @Test
    public void additionIsCorrect(){
        float result = my_cart.calculateTotal(100);
        assertEquals(100,result,0.001);

    }

    private void assertEquals(double v, float result, double v1) {
    }




}

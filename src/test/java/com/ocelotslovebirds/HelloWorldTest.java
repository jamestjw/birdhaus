package com.ocelotslovebirds;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;


public class HelloWorldTest {
    @Test
    public void testGetFourOneTwo() {
        assertEquals(412, HelloWorld.getFourOneTwo());
    }
}

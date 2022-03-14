package com.ocelotslovebirds.birdhaus.ticker;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;


public class FixedIntervalTickerTest {
    @Test
    public void alwaysTicks() {
        Ticker ticker = new FixedIntervalTicker(1);

        // We assume that if it works 10 times in a row,
        // it will continue to always work
        for (int i = 0; i < 10; i++) {
            assertTrue(ticker.tick());
        }
    }

    @Test
    public void ticksEveryOtherTime() {
        Ticker ticker = new FixedIntervalTicker(2);

        // We assume that if it works 5 times in a row,
        // it will continue to always work
        for (int i = 0; i < 5; i++) {
            assertFalse(ticker.tick());
            assertTrue(ticker.tick());
        }
    }

    @Test
    public void ticksEveryTenTicks() {
        Ticker ticker = new FixedIntervalTicker(10);

        // We assume that if it works 5 times in a row,
        // it will continue to always work
        for (int i = 0; i < 5; i++) {
            // Returns false 9 times
            assertFalse(ticker.tick());
            assertFalse(ticker.tick());
            assertFalse(ticker.tick());
            assertFalse(ticker.tick());
            assertFalse(ticker.tick());
            assertFalse(ticker.tick());
            assertFalse(ticker.tick());
            assertFalse(ticker.tick());
            assertFalse(ticker.tick());

            // Returns true once
            assertTrue(ticker.tick());
        }
    }
}

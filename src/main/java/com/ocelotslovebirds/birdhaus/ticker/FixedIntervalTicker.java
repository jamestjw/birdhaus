package com.ocelotslovebirds.birdhaus.ticker;

public class FixedIntervalTicker implements Ticker {
    private final int interval;
    private int counter;

    /**
     * @param interval How often the ticker should tick
     */
    public FixedIntervalTicker(int interval) {
        this.interval = interval;
        this.counter = 0;
    }

    public boolean tick() {
        this.counter++;

        if (this.counter == this.interval) {
            this.counter = 0;
            return true;
        }

        return false;
    }
}

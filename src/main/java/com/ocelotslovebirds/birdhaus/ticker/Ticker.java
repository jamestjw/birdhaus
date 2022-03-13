package com.ocelotslovebirds.birdhaus.ticker;

// A ticker class helps keep track of when events should happen
public interface Ticker {
    /*
        This function should be called for every tick that passes. It returns
        true when a particular tick matches the ticker's pattern. Patterns can
        be things like every 10 ticks or every other tick.
    */
    public boolean tick();
}

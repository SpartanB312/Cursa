package net.spartanb312.cursa.core.concurrent.utils;

/**
 * Created by B_312 on 05/01/2021
 */
public final class Timer {

    private long time;

    public Timer() {
        time = -1;
    }

    public boolean passed(int ms) {
        return ((System.currentTimeMillis() - this.time) >= ms);
    }

    public void reset() {
        time = System.currentTimeMillis();
    }

}
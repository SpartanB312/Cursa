package com.deneb.client.utils;

import java.time.Duration;
import java.time.Instant;

public final class Timer {

    public static Instant now() {
        return Instant.now();
    }

    private long time;

    public Timer()
    {
        time = -1;
    }

    public void resetTimeSkipTo(int ms) {
        this.time = System.currentTimeMillis() + ms;
    }

    public boolean passed(double ms) {
        return ((System.currentTimeMillis() - this.time) >= ms);
    }

    public boolean passedSec(double second) {
        return ((System.currentTimeMillis() - this.time) >= second * 1000);
    }

    public boolean passedTick(double tick) {
        return ((System.currentTimeMillis() - this.time) >= tick * 50);
    }

    public boolean passedMin(double minutes) {
        return ((System.currentTimeMillis() - this.time) >= minutes * 1000 * 60);
    }

    public static long getSecondsPassed(Instant start, Instant current) {
        return Duration.between(start, current).getSeconds();
    }

    public static boolean haveSecondsPassed(Instant start, Instant current, long seconds) {
        return Timer.getSecondsPassed(start, current) >= seconds;
    }

    public static long getMilliSecondsPassed(Instant start, Instant current) {
        return Duration.between(start, current).toMillis();
    }

    public static boolean haveMilliSecondsPassed(Instant start, Instant current, long milliseconds) {
        return Timer.getMilliSecondsPassed(start, current) >= milliseconds;
    }

    public void reset() {
        this.time = System.currentTimeMillis();
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

}
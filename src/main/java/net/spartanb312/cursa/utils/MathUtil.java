package net.spartanb312.cursa.utils;

public class MathUtil {
    public static int clamp(int val, final int min, final int max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }

    public static long clamp(long val, final long min, final long max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }
}

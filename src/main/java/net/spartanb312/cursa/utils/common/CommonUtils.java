package net.spartanb312.cursa.utils.common;

import net.spartanb312.cursa.utils.math.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUtils {

    /**
     * Collections
     */
    @SafeVarargs
    public  static  <T> List<T> listOf(T... elements) {
        return Arrays.asList(elements);
    }

    @SafeVarargs
    public  static  <T, U> Map<T, U> mapOf(Pair<T, U>... pairs) {
        Map<T, U> map = new HashMap<>();
        for (Pair<T, U> pair : pairs) {
            map.put(pair.a, pair.b);
        }
        return map;
    }

    @SafeVarargs
    public  static  <T> T[] arrayOf(T... elements) {
        return elements;
    }

    /**
     * Math
     */
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

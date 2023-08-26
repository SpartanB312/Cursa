package net.spartanb312.cursa.utils;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Easing {

    public static float converge(float from, float to, float speed) {
        if (from == to) return from;
        float speed0 = speed;
        boolean larger = to > from;
        if (speed0 < 0f) speed0 = 0f;
        else if (speed0 > 1f) speed0 = 1f;
        float dif = max(from, to) - min(from, to);
        float factor = dif * speed0;
        if (factor < 0.1f) factor = 0.1f;
        if (larger) return min(from + factor, to);
        else return max(from - factor, to);
    }

}

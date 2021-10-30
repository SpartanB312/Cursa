package net.spartanb312.cursa.utils;

import java.awt.*;

public class ColorUtil {

    public static Color getColor(int hex) {
        return new Color(hex);
    }

    public static int getAlpha(int hex) {
        return hex >> 24 & 255;
    }

    public static int getRed(int hex) {
        return hex >> 16 & 255;
    }

    public static int getGreen(int hex) {
        return hex >> 8 & 255;
    }

    public static int getBlue(int hex) {
        return hex & 255;
    }

    public static int getHoovered(int color, boolean isHoovered) {
        return isHoovered ? (color & 0x7F7F7F) << 1 : color;
    }

}

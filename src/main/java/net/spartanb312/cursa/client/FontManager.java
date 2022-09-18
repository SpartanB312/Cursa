package net.spartanb312.cursa.client;

import net.spartanb312.cursa.utils.graphics.font.UnicodeFontRenderer;

import java.awt.*;

public class FontManager {

    public static UnicodeFontRenderer iconFont;
    public static UnicodeFontRenderer fontRenderer;

    public static void init() {
        iconFont = UnicodeFontRenderer.create("/assets/minecraft/fonts/Icon.ttf", 50).setScale(0.32f);
        fontRenderer = UnicodeFontRenderer.create("/assets/minecraft/fonts/Microsoft YaHei UI.ttc", 100, 560, 16, 0.1f);
    }

    public static int getWidth(String str) {
        return (int) fontRenderer.getWidth(str);
    }

    public static int getHeight() {
        return (int) fontRenderer.getHeight();
    }

    public static void draw(String str, int x, int y, int color) {
        fontRenderer.drawString(str, x, y, new Color(color));
    }

    public static void draw(String str, int x, int y, Color color) {
        fontRenderer.drawString(str, x, y, color);
    }

    public static int getIconWidth() {
        return (int) iconFont.getWidth("q");
    }

    public static int getIconHeight() {
        return (int) iconFont.getHeight();
    }

    public static void drawIcon(int x, int y, int color) {
        iconFont.drawString("q", x, y, new Color(color));
    }

    public static void drawIcon(int x, int y, Color color) {
        iconFont.drawString("q", x, y, color);
    }

}

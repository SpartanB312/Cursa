package net.spartanb312.cursa.graphics;

import net.spartanb312.cursa.graphics.font.UnicodeFontRenderer;

import java.awt.*;

public class FontRenderers {

    public static UnicodeFontRenderer IconFont;
    public static UnicodeFontRenderer MainFontRenderer;
    public static UnicodeFontRenderer TitleFont;

    public static void init() {
        IconFont = UnicodeFontRenderer.create("/assets/minecraft/fonts/Icon.ttf", 50).setScale(0.32f);
        MainFontRenderer = UnicodeFontRenderer.create("/assets/minecraft/fonts/Microsoft YaHei UI.ttc", 40, 280, 16, 0.25f);
        TitleFont = UnicodeFontRenderer.create("/assets/minecraft/fonts/unoestado.ttf", 100, 560, 16, 0.1f);
    }

    public static int getWidth(String str) {
        return (int) MainFontRenderer.getWidth(str);
    }

    public static int getHeight() {
        return (int) MainFontRenderer.getHeight();
    }

    public static void draw(String str, int x, int y, int color) {
        MainFontRenderer.drawString(str, x, y, new Color(color));
    }

    public static void draw(String str, int x, int y, Color color) {
        MainFontRenderer.drawString(str, x, y, color);
    }

    public static int getIconWidth() {
        return (int) IconFont.getWidth("q");
    }

    public static int getIconHeight() {
        return (int) IconFont.getHeight();
    }

    public static void drawIcon(int x, int y, int color) {
        IconFont.drawString("q", x, y, new Color(color));
    }

    public static void drawIcon(int x, int y, Color color) {
        IconFont.drawString("q", x, y, color);
    }

}

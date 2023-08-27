package net.spartanb312.cursa.graphics.font;

import java.awt.*;

/**
 * Created by B_312 on 08/27/2023
 */
public class WordArt {

    public static final long startTime = System.currentTimeMillis();

    public static void drawRainbowString(IGradientFontRenderer fontRenderer, String str, float x, float y, float scale, int originX, float circleWidth, int timeGap) {
        drawRainbowStringWithShadow(fontRenderer, str, x, y, scale, 0f, originX, circleWidth, timeGap);
    }

    public static void drawRainbowStringWithShadow(IGradientFontRenderer fontRenderer, String str, float x, float y, float scale, float shadowDepth, int originX, float circleWidth, int timeGap) {
        Color[] colors = new Color[str.length() + 1];
        float currentX = x;
        int index = 0;
        for (char c : str.toCharArray()) {
            float offset = (currentX - originX) % circleWidth / circleWidth;
            float hue = (((int) (System.currentTimeMillis() - startTime)) % timeGap) / ((float) timeGap);
            colors[index] = new Color(Color.HSBtoRGB(hue - offset, 1.0f, 1.0f));
            currentX += fontRenderer.getWidth(String.valueOf(c), scale);
            index++;
        }
        float offset = (currentX - originX) % circleWidth / circleWidth;
        float hue = (((int) (System.currentTimeMillis() - startTime)) % timeGap) / ((float) timeGap);
        colors[index] = new Color(Color.HSBtoRGB(hue - offset, 1.0f, 1.0f));
        if (shadowDepth == 0) fontRenderer.drawGradientString(str, x, y, colors, scale);
        else fontRenderer.drawGradientStringWithShadow(str, x, y, shadowDepth, colors, scale);
    }

    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 1.0f, 1.0f).getRGB();
    }

}

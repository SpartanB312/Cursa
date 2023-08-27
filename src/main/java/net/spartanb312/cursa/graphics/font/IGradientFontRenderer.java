package net.spartanb312.cursa.graphics.font;

import java.awt.*;

public interface IGradientFontRenderer extends IFontRenderer {

    void drawGradientString(String text, float x, float y, Color[] colors);

    void drawGradientString(String text, float x, float y, Color[] colors, float scale);

    void drawGradientStringWithShadow(String text, float x, float y, Color[] colors);

    void drawGradientStringWithShadow(String text, float x, float y, Color[] colors, float scale);

    void drawGradientStringWithShadow(String text, float x, float y, float shadowDepth, Color[] colors);

    void drawGradientStringWithShadow(String text, float x, float y, float shadowDepth, Color[] colors, float scale);

}

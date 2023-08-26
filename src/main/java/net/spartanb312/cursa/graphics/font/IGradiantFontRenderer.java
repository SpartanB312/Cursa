package net.spartanb312.cursa.graphics.font;

import java.awt.*;

public interface IGradiantFontRenderer extends IFontRenderer {

    void drawGradiantString(String text, float x, float y, Color[] colors);

    void drawGradiantString(String text, float x, float y, Color[] colors, float scale);

    void drawGradiantStringWithShadow(String text, float x, float y, Color[] colors);

    void drawGradiantStringWithShadow(String text, float x, float y, Color[] colors, float scale);

    void drawGradiantStringWithShadow(String text, float x, float y, float shadowDepth, Color[] colors);

    void drawGradiantStringWithShadow(String text, float x, float y, float shadowDepth, Color[] colors, float scale);

}

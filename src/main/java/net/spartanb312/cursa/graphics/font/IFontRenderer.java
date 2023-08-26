package net.spartanb312.cursa.graphics.font;

import java.awt.*;

public interface IFontRenderer {

    float getHeight();

    float getHeight(float scale);

    float getWidth(String text);

    float getWidth(String text, float scale);

    void drawString(String text, float x, float y);

    void drawString(String text, float x, float y, int color);

    void drawString(String text, float x, float y, Color color);

    void drawString(String text, float x, float y, Color color, float scale);

    void drawStringWithShadow(String text, float x, float y);

    void drawStringWithShadow(String text, float x, float y, Color color);

    void drawStringWithShadow(String text, float x, float y, Color color, float scale);

    void drawStringWithShadow(String text, float x, float y, float shadowDepth);

    void drawStringWithShadow(String text, float x, float y, float shadowDepth, Color color);

    void drawStringWithShadow(String text, float x, float y, float shadowDepth, Color color, float scale);

}

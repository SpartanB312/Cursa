package net.spartanb312.cursa.graphics.font;

import java.awt.*;

public interface ICenteredFontRenderer extends IFontRenderer {

    void drawCenteredString(String text, float x, float y);

    void drawCenteredString(String text, float x, float y, int color);

    void drawCenteredString(String text, float x, float y, Color color);

    void drawCenteredString(String text, float x, float y, Color color, float scale);

    void drawCenteredStringWithShadow(String text, float x, float y);

    void drawCenteredStringWithShadow(String text, float x, float y, Color color);

    void drawCenteredStringWithShadow(String text, float x, float y, Color color, float scale);

    void drawCenteredStringWithShadow(String text, float x, float y, float shadowDepth);

    void drawCenteredStringWithShadow(String text, float x, float y, float shadowDepth, Color color);

    void drawCenteredStringWithShadow(String text, float x, float y, float shadowDepth, Color color, float scale);

}

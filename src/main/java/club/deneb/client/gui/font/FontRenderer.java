package club.deneb.client.gui.font;

import java.awt.*;

public interface FontRenderer {
    public int getFontHeight();
    public int getStringHeight(String text);
    public int getStringWidth(String text);
    public void drawString(int x, int y, String text);
    public void drawString(int x, int y, int r, int g, int b, String text);
    public void drawString(int x, int y, Color color, String text);
    public void drawString(int x, int y, int colour, String text);
    public void drawStringWithShadow(int x, int y, int r, int g, int b, String text);
}

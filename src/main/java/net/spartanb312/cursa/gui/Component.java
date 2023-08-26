package net.spartanb312.cursa.gui;

import net.spartanb312.cursa.graphics.FontRenderers;
import net.minecraft.client.Minecraft;
import net.spartanb312.cursa.graphics.font.UnicodeFontRenderer;

import java.awt.*;

/**
 * Created by B_312 on 01/10/21
 */
public abstract class Component {

    public UnicodeFontRenderer font = FontRenderers.MainFontRenderer;
    public Minecraft mc = Minecraft.getMinecraft();
    public int x, y, width, height;
    protected int fontColor = new Color(255, 255, 255).getRGB();
    public Panel father;
    public boolean isExtended;

    public abstract void render(int mouseX, int mouseY, float partialTicks);

    public abstract boolean mouseClicked(int mouseX, int mouseY, int mouseButton);

    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

    public void keyTyped(char typedChar, int keyCode) {
    }

    public void solvePos(boolean add) {
        this.x = father.x + (add ? 5 : 0);
        this.y = father.y;
    }

    public int getHoveredColor(int mouseX, int mouseY, int color) {
        if (isHovered(mouseX, mouseY)) {
            return (color & 0x7F7F7F) << 1;
        } else return color;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= Math.min(x, x + width) && mouseX <= Math.max(x, x + width) && mouseY >= Math.min(y, y + height) && mouseY <= Math.max(y, y + height);
    }

    public boolean isVisible() {
        return true;
    }

    public abstract String getDescription();

}

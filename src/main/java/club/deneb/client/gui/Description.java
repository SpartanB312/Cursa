package club.deneb.client.gui;

import club.deneb.client.Deneb;
import club.deneb.client.gui.font.CFontRenderer;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class Description {
    public static CFontRenderer font = Deneb.getINSTANCE().getFont();
    public static void runRender(String description,int mouseX,int mouseY){
        if(!description.equals("")) {
            int fontWidth = font.getStringWidth(description) + 4;
            Gui.drawRect(mouseX+5, mouseY, mouseX+5 + fontWidth,
                    mouseY + font.getHeight(), 0x85000000);
            font.drawString(description, mouseX + 7, (int) (mouseY + font.getHeight() / 2 - font.getHeight() / 2f) + 2, new Color(255, 255, 255).getRGB());
        }
    }
}

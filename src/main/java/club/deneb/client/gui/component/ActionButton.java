package club.deneb.client.gui.component;

import club.deneb.client.client.GuiManager;
import club.deneb.client.gui.Panel;
import club.deneb.client.utils.Utils;
import club.deneb.client.value.ButtonValue;
import net.minecraft.client.gui.Gui;

import java.awt.*;

import static club.deneb.client.utils.LambdaUtil.isHovered;

/**
 * Created by B_312 on 01/31/21
 */
public class ActionButton extends ValueButton<String>{

    public ActionButton(ButtonValue value, int width, int height, Panel father) {
        this.width = width;
        this.height = height;
        this.father = father;
        this.setValue(value);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        int color = GuiManager.getINSTANCE().getRGB();

        //Background
        Gui.drawRect(x, y, x + width, y + height, 0x85000000);

        int c =  color;

        if (isHovered(mouseX, mouseY).test(this)){
            c = (c & 0x7F7F7F) << 1;
        }

        font.drawString(getValue().getName(), x + 3, (int) (y + height / 2 - font.getHeight() / 2f) + 2, c);

        if(isHovered(mouseX,mouseY).test(this)){
            int fontWidth = font.getStringWidth(getValue().getDefaultValue()) + 4;
            Gui.drawRect(mouseX, mouseY, mouseX + fontWidth,
                    mouseY + height, 0x85000000);
            font.drawString(getValue().getName(), mouseX + 2, (int) (mouseY + height / 2 - font.getHeight() / 2f) + 2, new Color(255,255,255).getRGB());
        }
    }


    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!getValue().visible() || !isHovered(mouseX, mouseY).test(this))
            return false;
        if (mouseButton == 0) {
            ((ButtonValue)getValue()).runBinds();
            Utils.playButtonClick();
        }
        return true;
    }

}

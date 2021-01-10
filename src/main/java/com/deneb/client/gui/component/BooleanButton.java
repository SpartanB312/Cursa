package com.deneb.client.gui.component;

import com.deneb.client.client.GuiManager;
import com.deneb.client.gui.Panel;
import com.deneb.client.utils.Utils;
import com.deneb.client.value.BValue;
import com.deneb.client.value.Value;
import net.minecraft.client.gui.Gui;

import java.awt.*;

/**
 * Created by B_312 on 01/10/21
 */
public class BooleanButton extends ValueButton{

    boolean Enabled = false;

    public BooleanButton(Value value, int width, int height, Panel father) {
        this.width = width;
        this.height = height;
        this.father = father;
        this.setValue(value);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {


        int color = GuiManager.getINSTANCE().getRGB();
        int fontColor = new Color(255, 255, 255).getRGB();

        //Background
        Gui.drawRect(x, y, x + width, y + height, 0x85000000);

        int c = (Enabled ? color : fontColor);

        if (isHovered(mouseX,mouseY)){
            c = (c & 0x7F7F7F) << 1;
        }

        final BValue booleanValue = (BValue) getValue();

        if (Enabled) {
            booleanValue.setValue(true);
        } else {
            booleanValue.setValue(false);
        }

        font.drawString(booleanValue.getName(), x + 3, (int) (y + height / 2 - font.getHeight() / 2f) + 2, c);

    }


    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!getValue().visible() || !isHovered(mouseX, mouseY))
            return false;
        if (mouseButton == 0) {
            this.Enabled = ! Enabled;
            Utils.playButtonClick();
        }
        return true;
    }

}

package club.deneb.client.gui.component;

import club.deneb.client.gui.Panel;
import club.deneb.client.client.GuiManager;
import club.deneb.client.utils.Utils;
import club.deneb.client.value.Value;
import net.minecraft.client.gui.Gui;

import java.awt.*;

import static club.deneb.client.utils.LambdaUtil.isHovered;

/**
 * Created by B_312 on 01/10/21
 */
public class BooleanButton extends ValueButton<Boolean>{

    public BooleanButton(Value<Boolean> value, int width, int height, Panel father) {
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

        int c = (getSetting().getValue() ? color : fontColor);

        if (isHovered(mouseX, mouseY).test(this)){
            c = (c & 0x7F7F7F) << 1;
        }

        font.drawString(getSetting().getName(), x + 3, (int) (y + height / 2 - font.getHeight() / 2f) + 2, c);

    }


    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!getSetting().visible() || !isHovered(mouseX, mouseY).test(this))
            return false;
        if (mouseButton == 0) {
            this.getSetting().setValue(!getSetting().getValue());
            Utils.playButtonClick();
        }
        return true;
    }

}

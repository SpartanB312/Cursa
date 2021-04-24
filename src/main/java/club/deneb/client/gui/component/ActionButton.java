package club.deneb.client.gui.component;

import club.deneb.client.client.GuiManager;
import club.deneb.client.gui.Panel;
import club.deneb.client.utils.Utils;
import club.deneb.client.utils.clazz.Button;
import club.deneb.client.value.Value;
import net.minecraft.client.gui.Gui;

import static club.deneb.client.utils.LambdaUtil.isHovered;

/**
 * Created by B_312 on 01/31/21
 */
public class ActionButton extends ValueButton<Button>{

    public ActionButton(Value<Button> value, int width, int height, Panel father) {
        this.width = width;
        this.height = height;
        this.father = father;
        this.setValue(value);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {

        int color = GuiManager.INSTANCE.getRgb();

        //Background
        Gui.drawRect(x, y, x + width, y + height, 0x85000000);

        int c =  color;

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
            getSetting().getValue().runBinds();
            Utils.playButtonClick();
        }
        return true;
    }

}

package net.spartanb312.cursa.gui.components;

import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.core.setting.settings.EnumSetting;
import net.spartanb312.cursa.gui.Component;
import net.spartanb312.cursa.gui.Panel;
import net.spartanb312.cursa.utils.ColorUtil;
import net.spartanb312.cursa.utils.SoundUtil;
import net.spartanb312.cursa.graphics.RenderUtils2D;

import java.awt.*;

public class EnumButton extends Component {

    EnumSetting<?> setting;

    public EnumButton(Setting<? extends Enum<?>> setting, int width, int height, Panel father) {
        this.width = width;
        this.height = height;
        this.father = father;
        this.setting = (EnumSetting<?>) setting;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        RenderUtils2D.drawRect(x, y, x + width, y + height, 0x85000000);
        font.drawString(setting.getName(), x + 6, (int) (y + height / 2 - font.getHeight() / 2f), ColorUtil.getHoovered(new Color(255, 255, 255).getRGB(), isHovered(mouseX, mouseY)));
        font.drawString(setting.displayValue(),
                x + width - 3 - font.getWidth(setting.displayValue()), (int) (y + height / 2 - font.getHeight() / 2f),
                isHovered(mouseX, mouseY) ? fontColor : 0x909090);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isHovered(mouseX, mouseY) || !setting.isVisible()) return false;
        if (mouseButton == 0) {
            setting.forwardLoop();
            SoundUtil.playButtonClick();
        }
        return true;
    }

    @Override
    public boolean isVisible() {
        return setting.isVisible();
    }

    @Override
    public String getDescription() {
        return setting.getDescription();
    }

}

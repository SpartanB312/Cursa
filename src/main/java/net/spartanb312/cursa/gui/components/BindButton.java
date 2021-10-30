package net.spartanb312.cursa.gui.components;

import net.spartanb312.cursa.client.GUIManager;
import net.spartanb312.cursa.client.ModuleManager;
import net.spartanb312.cursa.core.common.KeyBind;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.gui.Component;
import net.spartanb312.cursa.gui.Panel;
import net.spartanb312.cursa.module.modules.client.ClickGUI;
import net.spartanb312.cursa.utils.SoundUtil;
import net.spartanb312.cursa.utils.graphics.RenderUtils2D;
import org.lwjgl.input.Keyboard;

public class BindButton extends Component {

    Setting<KeyBind> setting;
    boolean accepting = false;

    public BindButton(Setting<KeyBind>  setting, int width, int height, Panel father) {
        this.setting = setting;
        this.width = width;
        this.height = height;
        this.father = father;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        RenderUtils2D.drawRect(x, y, x + width, y + height, 0x85000000);
        font.drawString(accepting ? (setting.getName() + " | ...") : setting.getName() + " | " + (setting.getValue().getKeyCode() == 0x00 ? "NONE" : Keyboard.getKeyName(setting.getValue().getKeyCode())),
                x + 5, (int) (y + height / 2 - font.getHeight() / 2f) + 2, getHoveredColor(mouseX, mouseY, GUIManager.getColor3I()));
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (accepting) {
            if (keyCode == ModuleManager.getModule(ClickGUI.class).bindSetting.getValue().getKeyCode() || keyCode == Keyboard.KEY_BACK) {
                setting.getValue().setKeyCode(Keyboard.KEY_NONE);
            } else {
                setting.getValue().setKeyCode(keyCode);
            }
            accepting = false;
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isHovered(mouseX, mouseY))
            return false;

        if (mouseButton == 0) {
            accepting = true;
            SoundUtil.playButtonClick();
        } else if (mouseButton == 1) {
            setting.getValue().setKeyCode(Keyboard.KEY_NONE);
            SoundUtil.playAnvilHit();
        }
        return true;

    }

    @Override
    public String getDescription() {
        return setting.getDescription();
    }

}

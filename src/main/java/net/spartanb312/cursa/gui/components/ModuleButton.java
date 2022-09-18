package net.spartanb312.cursa.gui.components;

import net.spartanb312.cursa.client.FontManager;
import net.spartanb312.cursa.client.GUIManager;
import net.spartanb312.cursa.core.setting.NumberSetting;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.core.setting.settings.*;
import net.spartanb312.cursa.gui.Component;
import net.spartanb312.cursa.gui.Panel;
import net.spartanb312.cursa.module.ListenerSetting;
import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.utils.SoundUtil;
import net.spartanb312.cursa.utils.Timer;
import net.spartanb312.cursa.utils.graphics.RenderUtils2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton extends Component {

    public List<Component> settings = new ArrayList<>();
    public Module module;
    public Timer buttonTimer = new Timer();

    public ModuleButton(Module module, int width, int height, Panel father) {
        this.module = module;
        this.width = width;
        this.height = height;
        this.father = father;
        setup();
    }

    public void setup() {
        for (Setting<?> setting : module.getSettings()) {
            if (setting instanceof BooleanSetting)
                settings.add(new BooleanButton((BooleanSetting) setting, width, height, father));
            else if (setting instanceof IntSetting || setting instanceof FloatSetting || setting instanceof DoubleSetting)
                settings.add(new NumberSlider((NumberSetting<?>) setting, width, height, father));
            else if (setting instanceof EnumSetting)
                settings.add(new EnumButton((EnumSetting<?>) setting, width, height, father));
            else if (setting instanceof BindSetting)
                settings.add(new BindButton((BindSetting) setting, width, height, father));
            else if (setting instanceof ListenerSetting)
                settings.add(new ActionButton((ListenerSetting) setting, width, height, father));
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        RenderUtils2D.drawRect(x, y - 1, x + width, y + height + 1, 0x85000000);
        if (module.isEnabled())
            RenderUtils2D.drawRect(x + 1, y, x + width - 1, y + height, GUIManager.getColor4I());
        font.drawString(module.name, x + 3, (int) (y + height / 2 - font.getHeight() / 2f) , fontColor);
        FontManager.drawIcon(x + width - 2 - FontManager.getIconWidth(), y, new Color(230, 230, 230, 230));
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isHovered(mouseX, mouseY))
            return false;
        if (mouseButton == 0) {
            module.toggle();
            SoundUtil.playButtonClick();
        } else if (mouseButton == 1) {
            buttonTimer.reset();
            isExtended = !isExtended;
            SoundUtil.playButtonClick();
        }
        return true;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Component setting : settings) {
            setting.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        for (Component setting : settings) {
            setting.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public String getDescription() {
        return module.description;
    }

}

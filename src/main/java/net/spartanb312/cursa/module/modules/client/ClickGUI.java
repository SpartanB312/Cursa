package net.spartanb312.cursa.module.modules.client;

import net.spartanb312.cursa.client.ConfigManager;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.gui.CursaClickGUI;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;
import org.lwjgl.input.Keyboard;

@Parallel
@ModuleInfo(name = "ClickGUI", category = Category.CLIENT, keyCode = Keyboard.KEY_O, description = "ClickGUI of Cursa")
public class ClickGUI extends Module {

    public static ClickGUI instance;

    public ClickGUI() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            if (!(mc.currentScreen instanceof CursaClickGUI)) {
                mc.displayGuiScreen(new CursaClickGUI());
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen != null && mc.currentScreen instanceof CursaClickGUI) {
            mc.displayGuiScreen(null);
        }
        ConfigManager.saveAll();
    }

}

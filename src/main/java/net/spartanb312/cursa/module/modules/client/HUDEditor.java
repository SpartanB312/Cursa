package net.spartanb312.cursa.module.modules.client;

import net.spartanb312.cursa.client.ConfigManager;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.gui.CursaHUDEditor;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;
import org.lwjgl.input.Keyboard;

@Parallel
@ModuleInfo(name = "HUDEditor", category = Category.CLIENT, keyCode = Keyboard.KEY_GRAVE, description = "HUDEditor of Cursa")
public class HUDEditor extends Module {

    public static HUDEditor instance;

    public HUDEditor() {
        instance = this;
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            if (!(mc.currentScreen instanceof CursaHUDEditor)) {
                mc.displayGuiScreen(new CursaHUDEditor());
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen != null && mc.currentScreen instanceof CursaHUDEditor) {
            mc.displayGuiScreen(null);
        }
        ConfigManager.saveAll();
    }

}

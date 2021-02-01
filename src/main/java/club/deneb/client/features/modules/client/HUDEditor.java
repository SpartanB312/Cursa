package club.deneb.client.features.modules.client;

import club.deneb.client.gui.guis.HUDEditorScreen;
import club.deneb.client.client.ConfigManager;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import org.lwjgl.input.Keyboard;

@Module.Info(name = "HUDEditor",category = Category.CLIENT,keyCode = Keyboard.KEY_GRAVE,visible = false)
public class HUDEditor extends Module {
    public static HUDEditor INSTANCE;

    @Override
    public void onInit(){
        INSTANCE = this;
    }

    HUDEditorScreen screen;

    @Override
    public void onEnable() {
        if (screen == null){
            setGUIScreen(new HUDEditorScreen());
        }
        if (mc.player != null){
            if (!(mc.currentScreen instanceof HUDEditorScreen)) {
                mc.displayGuiScreen(screen);
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen != null && mc.currentScreen instanceof HUDEditorScreen) {
            mc.displayGuiScreen(null);
        }
        ConfigManager.saveAll();
    }

    private void setGUIScreen(HUDEditorScreen screen){
        this.screen = screen;
    }

}

package club.deneb.client.features.modules.client

import club.deneb.client.config.ConfigManager.saveAll
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.gui.guis.HUDEditorScreen
import org.lwjgl.input.Keyboard

@Module.Info(
    name = "HUDEditor",
    category = Category.CLIENT,
    keyCode = Keyboard.KEY_GRAVE,
    visible = false
)
class HUDEditor : Module() {

    var screen: HUDEditorScreen = HUDEditorScreen()

    override fun onEnable() {
        if (mc.player != null) {
            if (mc.currentScreen !is HUDEditorScreen) {
                mc.displayGuiScreen(screen)
            }
        }
    }

    override fun onDisable() {
        if (mc.currentScreen != null && mc.currentScreen is HUDEditorScreen) {
            mc.displayGuiScreen(null)
        }
        saveAll()
    }

}
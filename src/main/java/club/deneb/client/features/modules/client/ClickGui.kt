package club.deneb.client.features.modules.client

import club.deneb.client.client.ConfigManager.saveAll
import club.deneb.client.features.modules.client.ClickGui
import club.deneb.client.gui.guis.ClickGuiScreen
import club.deneb.client.features.AbstractModule
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import org.lwjgl.input.Keyboard

@Module.Info(
    name = "ClickGUI",
    category = Category.CLIENT,
    keyCode = Keyboard.KEY_I,
    visible = false
)
class ClickGui : Module() {

    var screen = ClickGuiScreen()

    override fun onEnable() {
        if (mc.player != null) {
            if (mc.currentScreen !is ClickGuiScreen) {
                mc.displayGuiScreen(screen)
            }
        }
    }

    override fun onDisable() {
        if (mc.currentScreen != null && mc.currentScreen is ClickGuiScreen) {
            mc.displayGuiScreen(null)
        }
        saveAll()
    }

}
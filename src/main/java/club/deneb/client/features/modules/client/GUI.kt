package club.deneb.client.features.modules.client

import club.deneb.client.value.ModeValue
import club.deneb.client.features.Category
import club.deneb.client.features.Module

@Module.Info(name = "GUI", category = Category.CLIENT, visible = false)
class GUI : Module() {

    val red = setting("Red", 255, 0, 255)
    val green = setting("Green", 0, 0, 255)
    val blue = setting("Blue", 0, 0, 255)
    val alpha = setting("Transparency", 200, 0, 255)
    val visibleButton = setting("VisibleButton", true)
    val resetButton = setting("ResetButton", true)
    val particle = setting("Particle", true)
    val rainbow = setting("Rainbow", false)
    val rainbowSpeed = setting("Rainbow Speed", 5.0f, 0.0f, 30.0f).b(rainbow)
    val rainbowSaturation = setting("Saturation", 0.65f, 0.0f, 1.0f).b(rainbow)
    val rainbowBrightness = setting("Brightness", 1.0f, 0.0f, 1.0f).b(rainbow)
    val background: ModeValue<String> = setting("Background", "Shadow", listOf("Shadow", "Blur", "Both", "None"))
    val setting: ModeValue<String> = setting("Setting", "Line", listOf("Rect", "Line", "None"))
    val iconMode: ModeValue<String> =
        setting("Icon", "Default", listOf("None", "Default", "Gear", "PaperPin", "Chain", "Pointer"))

    init {
        instance = this
    }

    companion object {
        lateinit var instance: GUI
    }
}
package club.deneb.client.client

import club.deneb.client.features.modules.client.Colors
import club.deneb.client.utils.ColorUtil
import java.awt.Color

/**
 * Author B_312 on 01/01/2021
 * Updated by B_312 on 04/24/2021
 */
object GuiManager {

    private val colorINSTANCE: Colors get() = Colors.getINSTANCE()
    private val isNull: Boolean get() = Colors.getINSTANCE() == null

    val normalRed: Int get() = if (isNull) 255 else colorINSTANCE.red.value
    val normalGreen: Int get() = if (isNull) 0 else colorINSTANCE.green.value
    val normalBlue: Int get() = if (isNull) 0 else colorINSTANCE.blue.value
    val normalRGB: Int get() = Color(normalRed, normalGreen, normalBlue).rgb

    val isRainbow: Boolean get() = !isNull && colorINSTANCE.rainbow.value
    val rainbowColor: Int
        get() {
            val hue =
                floatArrayOf(System.currentTimeMillis() % (360 * 32) / (360f * 32) * colorINSTANCE.rainbowSpeed.value)
            return Color.HSBtoRGB(hue[0], colorINSTANCE.rainbowSaturation.value, colorINSTANCE.rainbowBrightness.value)
        }

    val rainbowRed: Int get() = ColorUtil.getRed(rainbowColor)
    val rainbowGreen: Int get() = ColorUtil.getGreen(rainbowColor)
    val rainbowBlue: Int get() = ColorUtil.getBlue(rainbowColor)
    val red: Int get() = if (isRainbow) rainbowRed else normalRed
    val green: Int get() = if (isRainbow) rainbowGreen else normalGreen
    val blue: Int get() = if (isRainbow) rainbowBlue else normalBlue
    val rgb: Int get() = Color(red, green, blue).rgb

    val isParticle: Boolean get() = !isNull && colorINSTANCE.particle.value
    val isSettingRect: Boolean get() = !isNull && colorINSTANCE.setting.toggled("Rect")
    val isSettingSide: Boolean get() = !isNull && colorINSTANCE.setting.toggled("Side")

    val background: Background
        get() = when (colorINSTANCE.background.value) {
            "Shadow" -> {
                Background.Shadow
            }
            "Blur" -> {
                Background.Blur
            }
            "Both" -> {
                Background.Both
            }
            else -> Background.None
        }

    enum class Background {
        Shadow, Blur, Both, None
    }

    fun getBGColor(alpha: Int): Int {
        return Color(64, 64, 64, alpha).rgb
    }


}
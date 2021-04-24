package club.deneb.client.features.huds.info

import club.deneb.client.client.GuiManager.red
import club.deneb.client.client.GuiManager.green
import club.deneb.client.client.GuiManager.blue
import club.deneb.client.features.HUDModule
import club.deneb.client.utils.ChatUtil
import club.deneb.client.features.Category
import net.minecraft.util.math.MathHelper
import java.awt.Color
import java.text.DecimalFormat
import kotlin.math.pow

/**
 * Created by B_312 on 01/03/21
 */
@HUDModule.Info(name = "Speed", x = 180, y = 180, width = 100, height = 10, category = Category.INFO)
class Speed : HUDModule() {

    override fun onRender() {
        val fontColor = Color(red / 255f, green / 255f, blue / 255f, 1f).rgb
        val final = "km/h" + ChatUtil.SECTIONSIGN + "f " + speed()
        font.drawString(final, (x + 2).toFloat(), (y + 4).toFloat(), fontColor)
        height = font.height * 2
        width = font.getStringWidth(final) + 4
    }

    companion object {
        private val formatter = DecimalFormat("#.#")
        fun speed(): String {
            val currentTps = mc.timer.tickLength / 1000.0f
            val multiply = 3.6 // convert mps to kmh
            return formatter.format(
                MathHelper.sqrt(
                    coordsDiff('x').pow(2.0) + coordsDiff('z').pow(2.0)
                ) / currentTps * multiply
            )
        }

        private fun coordsDiff(s: Char): Double {
            return when (s) {
                'x' -> mc.player.posX - mc.player.prevPosX
                'z' -> mc.player.posZ - mc.player.prevPosZ
                else -> 0.0
            }
        }
    }
}
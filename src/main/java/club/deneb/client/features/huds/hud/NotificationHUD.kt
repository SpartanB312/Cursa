package club.deneb.client.features.huds.hud

import club.deneb.client.features.HUDModule
import club.deneb.client.client.NotificationManager.NotificationUnit
import club.deneb.client.client.NotificationManager.notificationUnitsList
import net.minecraft.client.gui.Gui
import club.deneb.client.utils.ChatUtil
import java.awt.Color

@HUDModule.Info(name = "Notification", x = 200, y = 200, width = 100, height = 100)
class NotificationHUD : HUDModule() {

    private val deadTime = setting("VanishTimer", 1000, 0, 5000)

    override fun onRender() {
        var maxWidth = 0
        val startX = x
        var startY = y

        notificationUnitsList.removeIf {
            it.timer.passed(
                deadTime.value.toDouble()
            )
        }
        for (unit in notificationUnitsList) {
            if (font.getStringWidth(getUnitString(unit)) + 4 > maxWidth) maxWidth =
                font.getStringWidth(getUnitString(unit)) + 4
            Gui.drawRect(
                startX,
                startY,
                startX + font.getStringWidth(getUnitString(unit)) + 4,
                startY + font.height * 2,
                -0x7b000000
            )
            Gui.drawRect(
                startX,
                startY + font.height * 2 - 1,
                (startX + getRate(unit)).toInt(),
                startY + font.height * 2,
                Color(16, 192, 255).rgb
            )
            font.drawString(
                getUnitString(unit),
                (startX + 1).toFloat(),
                (startY + 5).toFloat(),
                Color(255, 255, 255).rgb
            )
            startY += font.height * 2
        }
        width = if (maxWidth != 0) maxWidth else 100
        val temp = font.height * 2 * notificationUnitsList.size
        height = if (temp != 0) temp else 10
    }

    private fun getRate(unit: NotificationUnit): Double {
        return ((font.getStringWidth(getUnitString(unit)) + 4) * (deadTime.value - unit.timer.hasPassed()) / deadTime.value).toDouble()
    }

    private fun getUnitString(unit: NotificationUnit): String {
        return if (unit.enabled) unit.module.getName() + " " + ChatUtil.SECTIONSIGN + "a" + " Enabled" else unit.module.getName() + " " + ChatUtil.SECTIONSIGN + "c" + " Disabled"
    }
}
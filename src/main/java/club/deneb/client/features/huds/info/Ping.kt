package club.deneb.client.features.huds.info

import club.deneb.client.client.GuiManager.red
import club.deneb.client.client.GuiManager.green
import club.deneb.client.client.GuiManager.blue
import club.deneb.client.features.HUDModule
import club.deneb.client.features.Category
import club.deneb.client.utils.LagCompensator
import club.deneb.client.utils.ChatUtil
import java.awt.Color

@HUDModule.Info(name = "Ping", x = 170, y = 170, width = 100, height = 10, category = Category.INFO)
class Ping : HUDModule() {
    override fun onRender() {
        val fontColor = Color(red / 255f, green / 255f, blue / 255f, 1f).rgb
        val privatePingValue = LagCompensator.globalInfoPingValue()
        val final = "Ping " + ChatUtil.SECTIONSIGN + "f" + privatePingValue
        font.drawString(final, (x + 2).toFloat(), (y + 4).toFloat(), fontColor)
        height = font.height * 2
        width = font.getStringWidth(final) + 4
    }
}
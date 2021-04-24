package club.deneb.client.features.huds.info

import club.deneb.client.client.GuiManager.red
import club.deneb.client.client.GuiManager.green
import club.deneb.client.client.GuiManager.blue
import club.deneb.client.features.HUDModule
import club.deneb.client.features.Category
import club.deneb.client.utils.ChatUtil
import java.awt.Color

/**
 * Created by B_312 on 01/03/21
 */
@HUDModule.Info(name = "Players", x = 160, y = 160, width = 100, height = 10, category = Category.INFO)
class Players : HUDModule() {
    override fun onRender() {
        val fontColor = Color(red / 255f, green / 255f, blue / 255f, 1f).rgb
        val onlinePlayer = mc.player.connection.getPlayerInfoMap().size
        val final = "Player" + (if (onlinePlayer > 1) "s" else "") + " " + ChatUtil.SECTIONSIGN + "f" + onlinePlayer
        font.drawString(final, (x + 2).toFloat(), (y + 4).toFloat(), fontColor)
        height = font.height * 2
        width = font.getStringWidth(final) + 4
    }
}
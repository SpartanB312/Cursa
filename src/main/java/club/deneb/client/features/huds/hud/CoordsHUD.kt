package club.deneb.client.features.huds.hud

import club.deneb.client.client.GuiManager.red
import club.deneb.client.client.GuiManager.green
import club.deneb.client.client.GuiManager.blue
import club.deneb.client.features.HUDModule
import club.deneb.client.utils.ChatUtil
import java.awt.Color

/**
 * Created by B_312 on 01/03/21
 */
@HUDModule.Info(name = "CoordsHUD", x = 150, y = 150, width = 100, height = 10)
class CoordsHUD : HUDModule() {
    override fun onRender() {
        val fontColor = Color(red / 255f, green / 255f, blue / 255f, 1f).rgb
        val inHell = mc.player.dimension == -1
        val f = if (!inHell) 0.125f else 8.0f
        val posX = String.format("%.1f", mc.player.posX)
        val posY = String.format("%.1f", mc.player.posY)
        val posZ = String.format("%.1f", mc.player.posZ)
        val hPosX = String.format("%.1f", mc.player.posX * f.toDouble())
        val hPosZ = String.format("%.1f", mc.player.posZ * f.toDouble())
        val ow = "$posX, $posY, $posZ"
        val nether = "$hPosX, $posY, $hPosZ"
        val final =
            ChatUtil.SECTIONSIGN.toString() + "rXYZ " + ChatUtil.SECTIONSIGN + "f" + ow + ChatUtil.SECTIONSIGN + "r [" + ChatUtil.SECTIONSIGN + "f" + nether + ChatUtil.SECTIONSIGN + "r]"
        font.drawString(final, (x + 2).toFloat(), (y + 4).toFloat(), fontColor)
        height = font.height * 2
        width = font.getStringWidth(final) + 4
    }
}
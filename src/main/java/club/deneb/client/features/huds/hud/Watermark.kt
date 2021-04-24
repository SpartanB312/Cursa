package club.deneb.client.features.huds.hud

import club.deneb.client.Deneb
import club.deneb.client.client.GuiManager.blue
import club.deneb.client.client.GuiManager.green
import club.deneb.client.client.GuiManager.red
import club.deneb.client.features.HUDModule
import java.awt.Color

/**
 * Created by B_312 on 01/03/21
 */
@HUDModule.Info(name = "Watermark", x = 100, y = 100, width = 100, height = 10)
class Watermark : HUDModule() {
    override fun onRender() {
        val fontColor = Color(red / 255f, green / 255f, blue / 255f, 1f).rgb
        font.drawString(Deneb.MOD_NAME + " " + Deneb.VERSION, (x + 2).toFloat(), (y + 4).toFloat(), fontColor)
        height = font.height * 2
        width = font.getStringWidth(Deneb.MOD_NAME + " " + Deneb.VERSION) + 4
    }
}
package club.deneb.client.gui

import club.deneb.client.Deneb
import club.deneb.client.gui.font.CFontRenderer
import net.minecraft.client.gui.Gui
import java.awt.Color

object Description {

    var font: CFontRenderer = Deneb.getINSTANCE().font

    @JvmStatic
    fun runRender(description: String, mouseX: Int, mouseY: Int) {
        if (description != "") {
            val fontWidth = font.getStringWidth(description) + 4
            Gui.drawRect(
                mouseX + 5, mouseY, mouseX + 5 + fontWidth,
                mouseY + font.height, -0x7b000000
            )
            font.drawString(
                description,
                (mouseX + 7).toFloat(),
                ((mouseY + font.height / 2 - font.height / 2f).toInt() + 2).toFloat(),
                Color(255, 255, 255).rgb
            )
        }
    }
}
package club.deneb.client.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.gui.FontRenderer
import net.minecraft.world.World
import org.lwjgl.input.Keyboard

object Wrapper {
    var fontRenderer: FontRenderer = Minecraft.getMinecraft().fontRenderer
    @JvmStatic
    val player: EntityPlayerSP
        get() = Minecraft.getMinecraft().player
    @JvmStatic
    val world: World
        get() = Minecraft.getMinecraft().world

    @JvmStatic
    fun getKey(keyName: String): Int {
        return Keyboard.getKeyIndex(keyName.toUpperCase())
    }
}
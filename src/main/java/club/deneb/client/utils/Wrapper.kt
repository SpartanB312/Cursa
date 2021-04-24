package club.deneb.client.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.client.gui.FontRenderer
import net.minecraft.world.World
import org.lwjgl.input.Keyboard

object Wrapper {
    @JvmField
    var minecraft: Minecraft = Minecraft.getMinecraft()
    @JvmField
    var mc: Minecraft = Minecraft.getMinecraft()
    var fontRenderer: FontRenderer = minecraft.fontRenderer
    @JvmStatic
    val player: EntityPlayerSP
        get() = minecraft.player
    @JvmStatic
    val world: World
        get() = minecraft.world

    @JvmStatic
    fun getKey(keyName: String): Int {
        return Keyboard.getKeyIndex(keyName.toUpperCase())
    }
}
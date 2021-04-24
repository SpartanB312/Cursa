package club.deneb.client.command.commands

import club.deneb.client.command.Command
import club.deneb.client.utils.ChatUtil.sendNoSpamErrorMessage
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemShulkerBox
import net.minecraft.tileentity.TileEntityShulkerBox
import java.lang.Exception

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "peek", description = "Look inside the contents of a shulker box without opening it.")
class Peek : Command() {
    override fun onCall(s: String, vararg args: String) {
        val item = Minecraft.getMinecraft().player.inventory.getCurrentItem()
        try {
            if (item.getItem() is ItemShulkerBox) {
                val entityBox = TileEntityShulkerBox()
                entityBox.blockType = (item.getItem() as ItemShulkerBox).block
                entityBox.world = Minecraft.getMinecraft().world
                entityBox.readFromNBT(item.tagCompound!!.getCompoundTag("BlockEntityTag"))
                sb = entityBox
            } else {
                sendNoSpamErrorMessage("You aren't carrying a shulker box.")
            }
        } catch (e: Exception) {
            sendNoSpamErrorMessage(getSyntax())
        }
    }

    override fun getSyntax(): String {
        return "peek"
    }

    companion object {
        var sb: TileEntityShulkerBox? = null
    }
}
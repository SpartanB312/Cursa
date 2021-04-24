package club.deneb.client.features.modules.misc

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import club.deneb.client.event.events.client.GuiScreenEvent.Displayed
import net.minecraft.client.gui.GuiGameOver
import club.deneb.client.utils.ChatUtil
import club.deneb.client.features.Category
import club.deneb.client.features.Module

@Module.Info(
    name = "AutoRespawn",
    description = "Automatically respawns upon death and tells you where you died",
    category = Category.MISC
)
class AutoRespawn : Module() {
    private val deathCoords = setting("DeathCoords", false)
    private val respawn = setting("Respawn", true)
    @SubscribeEvent
    fun listener(event: Displayed) {
        if (event.screen is GuiGameOver) {
            if (deathCoords.value) ChatUtil.printChatMessage(
                String.format(
                    "You died at x %d y %d z %d",
                    mc.player.posX.toInt(),
                    mc.player.posY.toInt(),
                    mc.player.posZ.toInt()
                )
            )
            if (respawn.value) {
                mc.player.respawnPlayer()
                mc.displayGuiScreen(null)
            }
        }
    }
}
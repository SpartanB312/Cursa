package club.deneb.client.features.modules.player

import club.deneb.client.event.events.client.PacketEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraft.network.play.client.CPacketConfirmTeleport

@Module.Info(name = "PortalGodMode", category = Category.PLAYER)
class PortalGodMode : Module() {
    @SubscribeEvent
    fun onPacket(event: PacketEvent.Send) {
        if (isEnabled && event.getPacket() is CPacketConfirmTeleport) {
            event.cancel()
        }
    }
}
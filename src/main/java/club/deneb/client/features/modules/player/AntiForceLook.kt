package club.deneb.client.features.modules.player

import club.deneb.client.event.events.client.PacketEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraft.network.play.server.SPacketPlayerPosLook
import club.deneb.client.features.AbstractModule
import club.deneb.client.features.Category
import club.deneb.client.features.Module

@Module.Info(name = "AntiForceLook", category = Category.PLAYER)
class AntiForceLook : Module() {
    @SubscribeEvent
    fun onPacket(event: PacketEvent.Receive) {
        if (event.getPacket() is SPacketPlayerPosLook) {
            val packet = event.getPacket() as SPacketPlayerPosLook
            packet.yaw = mc.player.rotationYaw
            packet.pitch = mc.player.rotationPitch
        }
    }
}
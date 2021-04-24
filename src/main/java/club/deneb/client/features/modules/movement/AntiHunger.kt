package club.deneb.client.features.modules.movement

import club.deneb.client.event.events.client.PacketEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraft.network.play.client.CPacketPlayer

@Module.Info(
    name = "AntiHunger",
    category = Category.MOVEMENT,
    description = "Lose hunger less fast. Might cause ghostblocks."
)
class AntiHunger : Module() {
    @SubscribeEvent
    fun packetListener(event: PacketEvent.Send) {
        if (event.getPacket() is CPacketPlayer) {
            (event.getPacket() as CPacketPlayer).onGround = false
        }
    }
}
package club.deneb.client.features.modules.misc

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraft.network.play.client.CPacketChatMessage
import club.deneb.client.Deneb
import club.deneb.client.event.events.client.PacketEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module

@Module.Info(name = "CustomChat", category = Category.MISC, description = "Modifies your chat messages")
class CustomChat : Module() {
    private val commands = setting("Commands", false)
    @SubscribeEvent
    fun listener(event: PacketEvent.Send) {
        if (event.packet is CPacketChatMessage) {
            var s = (event.packet as CPacketChatMessage).getMessage()
            if (s.startsWith("/") && !commands.value) return
            s += Deneb.CHAT_SUFFIX
            if (s.length >= 256) s = s.substring(0, 256)
            (event.packet as CPacketChatMessage).message = s
        }
    }
}
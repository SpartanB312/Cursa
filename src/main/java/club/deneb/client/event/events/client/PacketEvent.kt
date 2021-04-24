package club.deneb.client.event.events.client

import club.deneb.client.event.MinecraftEvent
import net.minecraft.network.Packet
import net.minecraftforge.fml.common.eventhandler.Cancelable

@Cancelable
open class PacketEvent(var packet: Packet<*>?) : MinecraftEvent() {

    class Receive(packet: Packet<*>?) : PacketEvent(packet)
    class Send(packet: Packet<*>?) : PacketEvent(packet)
    override fun isCancelable(): Boolean {
        return true
    }
}
package club.deneb.client.features.modules.player

import club.deneb.client.event.events.client.PacketEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

@Module.Info(name = "Blink",description = "Cancel movement packet while it is enabled", category = Category.PLAYER)
class Blink : Module() {

    private var packets: Queue<CPacketPlayer> = LinkedList()

    @SubscribeEvent
    fun onPacket(event: PacketEvent.Send) {
        if (isEnabled && event.packet is CPacketPlayer) {
            event.cancel()
            packets.add(event.packet as CPacketPlayer)
        }
    }

    private var clonedPlayer: EntityOtherPlayerMP? = null
    override fun onEnable() {
        if (mc.player != null) {
            clonedPlayer = EntityOtherPlayerMP(mc.world, mc.getSession().profile)
            clonedPlayer!!.copyLocationAndAnglesFrom(mc.player)
            clonedPlayer!!.rotationYawHead = mc.player.rotationYawHead
            if(clonedPlayer != null) {
                mc.world.addEntityToWorld(-100, clonedPlayer!!)
            }
        }
    }

    override fun onDisable() {
        while (!packets.isEmpty()) mc.player.connection.sendPacket(packets.poll())
        val localPlayer: EntityPlayer? = mc.player
        if (localPlayer != null) {
            mc.world.removeEntityFromWorld(-100)
            clonedPlayer = null
        }
    }

    override fun getHudInfo(): String {
        return packets.size.toString()
    }
}
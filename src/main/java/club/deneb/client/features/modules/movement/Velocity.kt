package club.deneb.client.features.modules.movement

import club.deneb.client.event.events.client.PacketEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraft.network.play.server.SPacketEntityStatus
import net.minecraft.entity.projectile.EntityFishHook
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.network.play.server.SPacketExplosion

@Module.Info(name = "Velocity", category = Category.MOVEMENT)
class Velocity : Module() {

    private val horizontal = setting("Horizontal", 0, 0, 100)
    private val vertical = setting("Vertical", 0, 0, 100)
    private val explosions = setting("Explosions", true)
    private val bobbers = setting("Bobbers", true)
    private val mc = Minecraft.getMinecraft()

    @SubscribeEvent
    fun receivePacket(event: PacketEvent.Receive) {
        if (event.packet is SPacketEntityStatus && bobbers.value) {
            val packet = event.packet as SPacketEntityStatus
            if (packet.opCode.toInt() == 31) {
                val entity = packet.getEntity(mc.world)
                if (entity is EntityFishHook) {
                    if (entity.caughtEntity === mc.player) {
                        event.isCanceled = true
                    }
                }
            }
        }
        if (event.packet is SPacketEntityVelocity) {
            val packet = event.packet as SPacketEntityVelocity
            if (packet.getEntityID() == mc.player.getEntityId()) {
                if (horizontal.value == 0 && vertical.value == 0) {
                    event.isCanceled = true
                    return
                }
                if (horizontal.value != 100) {
                    packet.motionX = packet.motionX / 100 * horizontal.value
                    packet.motionZ = packet.motionZ / 100 * horizontal.value
                }
                if (vertical.value != 100) {
                    packet.motionY = packet.motionY / 100 * vertical.value
                }
            }
        }
        if (event.packet is SPacketExplosion && explosions.value) {
            val packet = event.packet as SPacketExplosion
            if (horizontal.value == 0 && vertical.value == 0) {
                event.isCanceled = true
                return
            }
            if (horizontal.value != 100) {
                packet.motionX = packet.motionX / 100 * horizontal.value
                packet.motionZ = packet.motionZ / 100 * horizontal.value
            }
            if (vertical.value != 100) {
                packet.motionY = packet.motionY / 100 * vertical.value
            }
        }
    }
}
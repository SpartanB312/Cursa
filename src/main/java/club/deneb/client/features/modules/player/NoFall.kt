package club.deneb.client.features.modules.player

import club.deneb.client.event.events.client.PacketEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.utils.EntityUtil
import net.minecraft.init.Items
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.util.EnumHand
import net.minecraft.util.math.RayTraceResult
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Module.Info(
    name = "NoFall",
    category = Category.PLAYER,
    description = "Prevents fall damage"
)
class NoFall : Module() {

    private val packet = setting("Packet", false)
    private val bucket = setting("Bucket", true)
    private val distance = setting("Distance", 15, 0, 256)
    private var last: Long = 0
    @SubscribeEvent
    fun onSendPacket(event: PacketEvent.Send) {
        if (event.packet is CPacketPlayer && packet.value) {
            (event.packet as CPacketPlayer).onGround = true
        }
    }

    override fun onTick() {
        if (bucket.value && mc.player.fallDistance >= distance.value && !EntityUtil.isAboveWater(mc.player) && System.currentTimeMillis() - last > 100) {
            val posVec = mc.player.positionVector
            val result = mc.world.rayTraceBlocks(posVec, posVec.add(0.0, -5.33, 0.0), true, true, false)
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                var hand = EnumHand.MAIN_HAND
                if (mc.player.heldItemOffhand.getItem() === Items.WATER_BUCKET) hand =
                    EnumHand.OFF_HAND else if (mc.player.heldItemMainhand.getItem() !== Items.WATER_BUCKET) {
                    for (i in 0..8) if (mc.player.inventory.getStackInSlot(i).getItem() === Items.WATER_BUCKET) {
                        mc.player.inventory.currentItem = i
                        mc.player.rotationPitch = 90f
                        last = System.currentTimeMillis()
                        return
                    }
                    return
                }
                mc.player.rotationPitch = 90f
                mc.playerController.processRightClick(mc.player, mc.world, hand)
                last = System.currentTimeMillis()
            }
        }
    }
}
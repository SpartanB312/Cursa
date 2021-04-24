package club.deneb.client.features.modules.player

import club.deneb.client.event.events.client.PacketEvent
import club.deneb.client.event.events.entity.PlayerMoveEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.network.play.client.CPacketInput
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Module.Info(
    name = "Freecam",
    description = "Leave your body and trascend into the realm of the gods",
    category = Category.PLAYER
)
class Freecam : Module() {

    private val speed = setting("Speed", 5, 0, 100)

    private var posX = 0.0
    private var posY = 0.0
    private var posZ = 0.0
    private var pitch = 0f
    private var yaw = 0f
    private var clonedPlayer: EntityOtherPlayerMP? = null
    private var isRidingEntity = false
    private var ridingEntity: Entity? = null

    override fun onEnable() {
        if (mc.player != null) {
            isRidingEntity = mc.player.getRidingEntity() != null
            if (mc.player.getRidingEntity() == null) {
                posX = mc.player.posX
                posY = mc.player.posY
                posZ = mc.player.posZ
            } else {
                ridingEntity = mc.player.getRidingEntity()
                mc.player.dismountRidingEntity()
            }
            pitch = mc.player.rotationPitch
            yaw = mc.player.rotationYaw
            clonedPlayer = EntityOtherPlayerMP(mc.world, mc.getSession().profile)
            clonedPlayer!!.copyLocationAndAnglesFrom(mc.player)
            clonedPlayer!!.rotationYawHead = mc.player.rotationYawHead
            if(clonedPlayer != null)
                mc.world.addEntityToWorld(-100, clonedPlayer!!)
            mc.player.capabilities.isFlying = true
            mc.player.capabilities.flySpeed = speed.value / 100f
            mc.player.noClip = true
        }
    }

    override fun onDisable() {
        val localPlayer: EntityPlayer? = mc.player
        if (localPlayer != null) {
            mc.player.setPositionAndRotation(posX, posY, posZ, yaw, pitch)
            mc.world.removeEntityFromWorld(-100)
            clonedPlayer = null
            posZ = 0.0
            posY = posZ
            posX = posY
            yaw = 0f
            pitch = yaw
            mc.player.capabilities.isFlying = false //getModManager().getMod("ElytraFlight").isEnabled();
            mc.player.capabilities.flySpeed = 0.05f
            mc.player.noClip = false
            mc.player.motionZ = 0.0
            mc.player.motionY = mc.player.motionZ
            mc.player.motionX = mc.player.motionY
            if (isRidingEntity && ridingEntity != null) {
                mc.player.startRiding(ridingEntity!!, true)
            }
        }
    }

    override fun onTick() {
        mc.player.capabilities.isFlying = true
        mc.player.capabilities.flySpeed = speed.value / 100f
        mc.player.noClip = true
        mc.player.onGround = false
        mc.player.fallDistance = 0f
    }

    @SubscribeEvent
    fun moveListener(event: PlayerMoveEvent?) {
        mc.player.noClip = true
    }

    @SubscribeEvent
    fun pushListener(event: PlayerSPPushOutOfBlocksEvent) {
        event.isCanceled = true
    }

    @SubscribeEvent
    fun sendListener(event: PacketEvent.Send) {
        if (event.getPacket() is CPacketPlayer || event.getPacket() is CPacketInput) {
            event.cancel()
        }
    }
}
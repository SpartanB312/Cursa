package club.deneb.client.features.modules.movement

import club.deneb.client.utils.WorldTimer
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.client.event.InputUpdateEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.item.ItemShield
import net.minecraft.network.play.client.CPacketPlayerDigging
import net.minecraft.util.math.BlockPos
import net.minecraft.init.Blocks
import net.minecraft.util.math.MathHelper
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Module.Info(
    name = "Speed",
    description = "Make you move faster than vanilla player",
    category = Category.MOVEMENT
)
class Speed : Module() {

    private val speed = setting("Speed", 1.0, 0.0, 50.0)
    private val airMotion = setting("InAirMotion", true)
    private val autoJump = setting("AutoJump", true)
    private val useTimer = setting("UseTimer", true)
    private val iceSpeed = setting("IceSpeed", false)
    private val slipperiness = setting("Slipperiness", 0.4f, 0.2f, 1.0f).b(iceSpeed)

    private var waitCounter = 0
    private val timers = WorldTimer()

    @SubscribeEvent
    fun onInput(event: InputUpdateEvent?) {
        if (mc.player.isHandActive && !mc.player.isRiding) {
            mc.player.movementInput.moveStrafe /= 0.2f
            mc.player.movementInput.moveForward /= 0.2f
        }
    }

    override fun onTick() {
        if (mc.player.isRiding) {
            return
        }
        if (mc.player.capabilities != null) {
            if (mc.player.capabilities.isFlying || mc.player.isElytraFlying) return
        }
        if (mc.player.isHandActive) {
            if (mc.player.getHeldItem(mc.player.getActiveHand()).getItem() is ItemShield) {
                if (mc.player.movementInput.moveStrafe != 0f || mc.player.movementInput.moveForward != 0f && mc.player.itemInUseMaxCount >= 8) {
                    mc.player.connection.sendPacket(
                        CPacketPlayerDigging(
                            CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
                            BlockPos.ORIGIN,
                            mc.player.horizontalFacing
                        )
                    )
                }
            }
        }
        if (iceSpeed.value) {
            Blocks.ICE.slipperiness = slipperiness.value
            Blocks.PACKED_ICE.slipperiness = slipperiness.value
            Blocks.FROSTED_ICE.slipperiness = slipperiness.value
        }
        if (useTimer.value) {
            timers.setOverrideSpeed(1.088f)
        }
        val boost = abs(mc.player.rotationYawHead - mc.player.rotationYaw) < 90
        if (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) {
            //Sprint
            if (!mc.player.isSprinting) {
                mc.player.isSprinting = true
            }
            if (mc.player.onGround) {
                waitCounter = if (waitCounter < 1) {
                    waitCounter++
                    return
                } else {
                    0
                }
                val yaw = playerDirection
                if (autoJump.value) mc.player.jump()
                //mc.player.motionY = 0.405;
                mc.player.motionX -= MathHelper.sin(yaw) * 0.005f * speed.value
                mc.player.motionZ += MathHelper.cos(yaw) * 0.005f * speed.value
            } else if (airMotion.value) {
                val direction = playerDirection
                val currentSpeed =
                    sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ)
                var speed = if (boost) 1.0064 else 1.001
                if (mc.player.motionY < 0) speed = 1.0
                mc.player.motionX = -sin(direction.toDouble()) * speed * currentSpeed
                mc.player.motionZ = cos(direction.toDouble()) * speed * currentSpeed
            }
        } else {
            mc.player.motionX = 0.0
            mc.player.motionZ = 0.0
        }
    }

    override fun onDisable() {
        if (useTimer.value) {
            timers.resetTime()
        }
        if (iceSpeed.value) {
            Blocks.ICE.slipperiness = 0.98f
            Blocks.PACKED_ICE.slipperiness = 0.98f
            Blocks.FROSTED_ICE.slipperiness = 0.98f
        }
    }

    override fun getHudInfo(): String {
        return ""
    }

    companion object {
        private val playerDirection: Float
            get() {
                var rotationYaw = mc.player.rotationYaw
                if (mc.player.moveForward < 0.0f) {
                    rotationYaw += 180.0f
                }
                var forward = 1.0f
                if (mc.player.moveForward < 0.0f) {
                    forward = -0.5f
                } else if (mc.player.moveForward > 0.0f) {
                    forward = 0.5f
                }
                if (mc.player.moveStrafing > 0.0f) {
                    rotationYaw -= 90.0f * forward
                }
                if (mc.player.moveStrafing < 0.0f) {
                    rotationYaw += 90.0f * forward
                }
                return rotationYaw * 0.017453292f
            }
    }
}
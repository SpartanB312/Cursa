package club.deneb.client.features.modules.player

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.value.ModeValue
import net.minecraft.util.math.MathHelper
import java.util.*
import kotlin.math.roundToInt

@Module.Info(
    name = "YawPitchLock",
    description = "Lock your camera",
    category = Category.PLAYER
)
class YawPitchLock : Module() {

    private val mode: ModeValue<String> = setting("Mode", "Yaw&Pitch", listOf("Yaw&Pitch", "Yaw", "Pitch"))
    private val yawAuto = setting("YawAuto", true)
    private val yaw = setting("YawValue", 180, 0, 360)
    private val yawSlice = setting("YawSlice", 8, 1, 36)
    private val pitchAuto = setting("PitchAuto", true)
    private val pitch = setting("PitchValue", 180, 0, 360)
    private val pitchSlice = setting("YawSlice", 8, 1, 36)

    override fun onTick() {
        if (mode.toggled("Yaw") || mode.toggled("Yaw&Pitch")) {
            if (yawAuto.value) {
                val angle = 360 / yawSlice.value
                var yaw = mc.player.rotationYaw
                yaw = ((yaw / angle).roundToInt() * angle).toFloat()
                mc.player.rotationYaw = yaw
                if (mc.player.isRiding) Objects.requireNonNull(mc.player.getRidingEntity())!!.rotationYaw = yaw
            } else {
                mc.player.rotationYaw = MathHelper.clamp(yaw.value - 180, -180, 180).toFloat()
            }
        }
        if (mode.toggled("Pitch") || mode.toggled("Yaw&Pitch")) {
            if (pitchAuto.value) {
                val angle = 360 / pitchSlice.value
                var yaw = mc.player.rotationPitch
                yaw = ((yaw / angle).roundToInt() * angle).toFloat()
                mc.player.rotationPitch = yaw
                if (mc.player.isRiding) Objects.requireNonNull(mc.player.getRidingEntity())!!.rotationPitch = yaw
            } else {
                mc.player.rotationPitch = MathHelper.clamp(pitch.value - 180, -180, 180).toFloat()
            }
        }
    }
}
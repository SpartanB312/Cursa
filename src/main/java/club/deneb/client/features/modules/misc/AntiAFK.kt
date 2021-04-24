package club.deneb.client.features.modules.misc

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.network.play.client.CPacketAnimation
import net.minecraft.util.EnumHand
import java.util.*

@Module.Info(
    name = "AntiAFK",
    category = Category.MISC,
    description = "Moves in order not to get kicked. (May be invisible client-sided)"
)
class AntiAFK : Module() {
    private val swing = setting("Swing", true)
    private val turn = setting("Turn", true)
    private val random = Random()

    override fun onTick() {
        if (mc.playerController.getIsHittingBlock()) return
        if (mc.player.ticksExisted % 40 == 0 && swing.value) Objects.requireNonNull(mc.connection)!!
            .sendPacket(CPacketAnimation(EnumHand.MAIN_HAND))
        if (mc.player.ticksExisted % 15 == 0 && turn.value) mc.player.rotationYaw =
            (random.nextInt(360) - 180).toFloat()
        if (!(swing.value || turn.value) && mc.player.ticksExisted % 80 == 0) {
            mc.player.jump()
        }
    }
}
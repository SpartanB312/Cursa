package club.deneb.client.features.modules.player

import club.deneb.client.features.Category
import club.deneb.client.features.Module

/**
 * Created by 086 on 24/12/2017.
 */
@Module.Info(
    name = "AutoJump",
    description = "Automatically jumps if possible",
    category = Category.PLAYER
)
class AutoJump : Module() {
    override fun onTick() {
        if (mc.player.isInWater || mc.player.isInLava) mc.player.motionY =
            0.1 else if (mc.player.onGround) mc.player.jump()
    }
}
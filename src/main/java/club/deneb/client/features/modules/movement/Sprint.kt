package club.deneb.client.features.modules.movement

import club.deneb.client.features.Category
import club.deneb.client.features.Module

@Module.Info(
    name = "Sprint",
    description = "Automatically makes the player sprint",
    category = Category.MOVEMENT
)
class Sprint : Module() {
    override fun onTick() {
        try {
            mc.player.isSprinting = !mc.player.collidedHorizontally && mc.player.moveForward > 0
        } catch (ignored: Exception) {
        }
    }
}
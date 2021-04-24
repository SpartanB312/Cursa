package club.deneb.client.features.modules.player

import club.deneb.client.features.Category
import club.deneb.client.features.Module

@Module.Info(name = "FastPlace", category = Category.PLAYER, description = "Nullifies block place delay")
class FastPlace : Module() {
    override fun onTick() {
        mc.rightClickDelayTimer = 0
    }
}
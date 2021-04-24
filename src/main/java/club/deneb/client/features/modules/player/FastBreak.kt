package club.deneb.client.features.modules.player

import club.deneb.client.features.Category
import club.deneb.client.features.Module

@Module.Info(name = "FastBreak", category = Category.PLAYER, description = "Nullifies block hit delay")
class FastBreak : Module() {
    override fun onTick() {
        mc.playerController.blockHitDelay = 0
    }
}
package club.deneb.client.features.modules.misc

import club.deneb.client.features.Category
import club.deneb.client.features.Module

@Module.Info(name = "AntiWeather", description = "Removes rain from your world", category = Category.MISC)
class AntiWeather : Module() {
    override fun onTick() {
        if (isDisabled) return
        if (mc.world.isRaining) mc.world.setRainStrength(0f)
    }
}
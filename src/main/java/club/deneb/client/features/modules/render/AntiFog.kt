package club.deneb.client.features.modules.render

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.features.ModuleManager
import club.deneb.client.value.ModeValue

@Module.Info(
    name = "AntiFog",
    description = "Disables or reduces fog",
    category = Category.RENDER
)
class AntiFog : Module() {

    init {
        mode = setting("Mode", "NoFog", listOf("NoFog", "Air"))
    }

    companion object {
        lateinit var mode: ModeValue<String>
        @JvmStatic
        fun enabled(): Boolean {
            return ModuleManager.getModule(AntiFog::class.java).isEnabled
        }
    }
}
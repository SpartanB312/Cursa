package club.deneb.client.features.modules.render

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.features.modules.render.NoHurtCam

@Module.Info(
    name = "NoHurtCam",
    description = "Disables the 'hurt' camera effect",
    category = Category.RENDER
)
class NoHurtCam : Module() {

    init {
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE:NoHurtCam
        @JvmStatic
        fun shouldDisable(): Boolean {
            return INSTANCE.isEnabled
        }
    }

}
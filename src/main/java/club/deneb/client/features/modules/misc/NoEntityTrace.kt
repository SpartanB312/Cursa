package club.deneb.client.features.modules.misc

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.features.ModuleManager
import club.deneb.client.features.modules.misc.NoEntityTrace
import club.deneb.client.value.ModeValue

@Module.Info(
    name = "NoEntityTrace",
    category = Category.MISC,
    description = "Blocks entities from stopping you from mining"
)
class NoEntityTrace : Module() {

    var mode: ModeValue<String> = setting("Mode", "Dynamic", listOf("Dynamic", "Static"))

    companion object {
        @JvmStatic
        fun shouldBlock(): Boolean {
            val instance:NoEntityTrace = ModuleManager.getModule(NoEntityTrace::class.java) as NoEntityTrace
            return instance.isEnabled && (instance.mode.toggled("Static") || mc.playerController.isHittingBlock)
        }
    }

}
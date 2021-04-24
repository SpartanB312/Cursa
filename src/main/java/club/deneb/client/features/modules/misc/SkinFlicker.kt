package club.deneb.client.features.modules.misc

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.value.ModeValue
import net.minecraft.entity.player.EnumPlayerModelParts
import java.util.*

@Module.Info(
    name = "SkinFlicker",
    description = "Toggle the jacket layer rapidly for a cool skin effect",
    category = Category.MISC
)
class SkinFlicker : Module() {

    private val mode: ModeValue<String> = setting("Mode", "HORIZONTAL", listOf("HORIZONTAL", "VERTICAL", "RANDOM"))
    private val slowness = setting("Slowness", 2, 1, 10)
    private val r = Random()
    private val len = EnumPlayerModelParts.values().size

    override fun onTick() {
        when (mode.value) {
            "RANDOM" -> {
                if (mc.player.ticksExisted % slowness.value != 0) return
                mc.gameSettings.switchModelPartEnabled(EnumPlayerModelParts.values()[r.nextInt(len)])
            }
            "VERTICAL", "HORIZONTAL" -> {
                var i = mc.player.ticksExisted / slowness.value % (PARTS_HORIZONTAL.size * 2) // *2 for on/off
                var on = false
                if (i >= PARTS_HORIZONTAL.size) {
                    on = true
                    i -= PARTS_HORIZONTAL.size
                }
                mc.gameSettings.setModelPartEnabled(
                    if (mode.value == "VERTICAL") PARTS_VERTICAL[i] else PARTS_HORIZONTAL[i],
                    on
                )
            }
        }
    }

    companion object {
        private val PARTS_HORIZONTAL = arrayOf(
            EnumPlayerModelParts.LEFT_SLEEVE,
            EnumPlayerModelParts.JACKET,
            EnumPlayerModelParts.HAT,
            EnumPlayerModelParts.LEFT_PANTS_LEG,
            EnumPlayerModelParts.RIGHT_PANTS_LEG,
            EnumPlayerModelParts.RIGHT_SLEEVE
        )
        private val PARTS_VERTICAL = arrayOf(
            EnumPlayerModelParts.HAT,
            EnumPlayerModelParts.JACKET,
            EnumPlayerModelParts.LEFT_SLEEVE,
            EnumPlayerModelParts.RIGHT_SLEEVE,
            EnumPlayerModelParts.LEFT_PANTS_LEG,
            EnumPlayerModelParts.RIGHT_PANTS_LEG
        )
    }
}
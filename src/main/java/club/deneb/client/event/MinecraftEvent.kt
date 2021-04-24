package club.deneb.client.event

import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.eventhandler.Event

open class MinecraftEvent : Event() {
    var isCancelled = false
    var era = Era.PRE
    val partialTicks: Float = Minecraft.getMinecraft().renderPartialTicks
    fun cancel() {
        isCancelled = true
    }

    enum class Era {
        PRE, PERI, POST
    }
}
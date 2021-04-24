package club.deneb.client.event

import club.deneb.client.utils.Wrapper
import net.minecraftforge.fml.common.eventhandler.Event

open class MinecraftEvent : Event() {
    var isCancelled = false
    var era = Era.PRE
    val partialTicks: Float = Wrapper.mc.renderPartialTicks
    fun cancel() {
        isCancelled = true
    }

    enum class Era {
        PRE, PERI, POST
    }
}
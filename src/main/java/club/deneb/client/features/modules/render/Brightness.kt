package club.deneb.client.features.modules.render

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import java.util.*
import java.util.function.Function
import kotlin.math.sin

@Module.Info(name = "Brightness", description = "Makes everything brighter!", category = Category.RENDER)
class Brightness : Module() {

    private val transition = setting("Transition", true)
    private val seconds = setting("Seconds", 1f, 0f, 10f).b(transition)
    private val mode = setting("Mode", "Sine", listOf("Sine", "Liner")).b(transition)

    private val transitionStack = Stack<Float>()
    private fun addTransition(isUpwards: Boolean) {
        if (transition.value) {
            val length = (seconds.value * 20).toInt()
            val values: FloatArray = when (mode.value) {
                "Liner" -> linear(length, isUpwards)
                "Sine" -> sine(length, isUpwards)
                else -> floatArrayOf(0f)
            }
            for (v in values) {
                transitionStack.add(v)
            }
            isInTransition = true
        }
    }

    override fun onEnable() {
        super.onEnable()
        addTransition(true)
    }

    override fun onDisable() {
        super.onDisable()
        addTransition(false)
    }

    override fun onTick() {
        if (isInTransition) {
            if (transitionStack.isEmpty()) {
                isInTransition = false
                currentBrightness = if (isEnabled) 1f else 0f
            } else {
                currentBrightness = transitionStack.pop()
            }
        }
    }

    private fun createTransition(length: Int, upwards: Boolean, function: Function<Float, Float>): FloatArray {
        val transition = FloatArray(length)
        for (i in 0 until length) {
            var v = function.apply(i.toFloat() / length.toFloat())
            if (upwards) v = 1 - v
            transition[i] = v
        }
        return transition
    }

    private fun linear(length: Int, polarity: Boolean): FloatArray { // length of 20 = 1 second
        return createTransition(length, polarity) { d: Float -> d }
    }

    private fun sine(x: Float): Float { // (sin(pi*x-(pi/2)) + 1) / 2
        return (sin(Math.PI * x - Math.PI / 2).toFloat() + 1) / 2
    }

    private fun sine(length: Int, polarity: Boolean): FloatArray {
        return createTransition(length, polarity) { x: Float -> this.sine(x) }
    }

    companion object {
        @JvmStatic
        var currentBrightness = 0f
            private set
        var isInTransition = false
            private set

        @JvmStatic
        fun shouldBeActive(): Boolean {
            return isInTransition || currentBrightness == 1f // if in transition or enabled
        }
    }
}
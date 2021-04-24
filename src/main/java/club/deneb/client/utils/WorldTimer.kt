package club.deneb.client.utils

class WorldTimer {
    private var overrideSpeed = 1.0f
    private fun useTimer() {
        if (overrideSpeed != 1.0f && overrideSpeed > 0.1f) {
            Wrapper.minecraft.timer.tickLength = 50.0f / overrideSpeed
        }
    }

    fun setOverrideSpeed(f: Float) {
        overrideSpeed = f
        useTimer()
    }

    fun resetTime() {
        Wrapper.minecraft.timer.tickLength = 50f
    }
}
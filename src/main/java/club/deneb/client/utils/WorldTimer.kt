package club.deneb.client.utils

import net.minecraft.client.Minecraft

class WorldTimer {
    private var overrideSpeed = 1.0f
    private fun useTimer() {
        if (overrideSpeed != 1.0f && overrideSpeed > 0.1f) {
            Minecraft.getMinecraft().timer.tickLength = 50.0f / overrideSpeed
        }
    }

    fun setOverrideSpeed(f: Float) {
        overrideSpeed = f
        useTimer()
    }

    fun resetTime() {
        Minecraft.getMinecraft().timer.tickLength = 50f
    }
}
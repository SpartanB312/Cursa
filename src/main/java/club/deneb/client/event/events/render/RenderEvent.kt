package club.deneb.client.event.events.render

import net.minecraft.client.renderer.Tessellator
import club.deneb.client.event.MinecraftEvent
import net.minecraft.client.renderer.BufferBuilder
import net.minecraft.util.math.Vec3d

class RenderEvent(private val tessellator: Tessellator, private val renderPos: Vec3d) : MinecraftEvent() {
    private val buffer: BufferBuilder
        get() = tessellator.buffer

    fun setTranslation(translation: Vec3d) {
        buffer.setTranslation(-translation.x, -translation.y, -translation.z)
    }

    fun resetTranslation() {
        setTranslation(renderPos)
    }
}
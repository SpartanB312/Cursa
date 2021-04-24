package club.deneb.client.features.modules.player

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.features.ModuleManager
import club.deneb.client.features.modules.render.PathFind
import club.deneb.client.utils.EntityUtil
import club.deneb.client.value.ModeValue
import net.minecraft.pathfinding.PathPoint
import net.minecraftforge.client.event.InputUpdateEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Module.Info(name = "AutoWalk", category = Category.PLAYER)
class AutoWalk : Module() {

    private val mode: ModeValue<String> = setting("Mode", "Forward", listOf("Forward", "Backwards", "Path"))
    @SubscribeEvent
    fun onInput(event: InputUpdateEvent) {
        when (mode.value) {
            "Forward" -> event.movementInput.moveForward = 1f
            "Backwards" -> event.movementInput.moveForward = -1f
            "Path" -> {
                if (PathFind.points.isEmpty()) return
                event.movementInput.moveForward = 1f
                if (mc.player.isInWater || mc.player.isInLava) mc.player.movementInput.jump =
                    true else if (mc.player.collidedHorizontally && mc.player.onGround) mc.player.jump()
                if (!ModuleManager.getModuleByName("Pathfind").isEnabled || PathFind.points.isEmpty()) return
                val next = PathFind.points[0]
                lookAt(next)
            }
        }
    }

    private fun lookAt(pathPoint: PathPoint) {
        val v = EntityUtil.calculateLookAt(
            (pathPoint.x + .5f).toDouble(),
            pathPoint.y.toDouble(),
            (pathPoint.z + .5f).toDouble(),
            mc.player
        )
        mc.player.rotationYaw = v[0].toFloat()
        mc.player.rotationPitch = v[1].toFloat()
    }
}
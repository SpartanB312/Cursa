package club.deneb.client.utils

import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d

object MathsUtils {
    fun normalizeAngle(angleIn: Double): Double {
        var angle = angleIn
        angle %= 360.0
        if (angle >= 180.0) {
            angle -= 360.0
        }
        if (angle < -180.0) {
            angle += 360.0
        }
        return angle
    }

    fun interpolateEntity(entity: Entity, time: Float): Vec3d {
        return Vec3d(
            entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time,
            entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time,
            entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time
        )
    }
}
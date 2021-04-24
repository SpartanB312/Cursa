package club.deneb.client.utils

import net.minecraft.client.Minecraft
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.BlockPos
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.Vec3d
import kotlin.math.atan2

object RotationUtil {
    fun getRotations(from: Vec3d, to: Vec3d): FloatArray {
        val difX = to.x - from.x
        val difY = (to.y - from.y) * -1.0
        val difZ = to.z - from.z
        val dist = MathHelper.sqrt(difX * difX + difZ * difZ).toDouble()
        return floatArrayOf(
            MathHelper.wrapDegrees(Math.toDegrees(atan2(difZ, difX)) - 90.0).toFloat(), MathHelper.wrapDegrees(
                Math.toDegrees(
                    atan2(difY, dist)
                )
            )
                .toFloat()
        )
    }

    fun getRotationsBlock(block: BlockPos, face: EnumFacing, Legit: Boolean): FloatArray {
        val x = block.getX() + 0.5 - Minecraft.getMinecraft().player.posX + face.xOffset
            .toDouble() / 2
        val z = block.getZ() + 0.5 - Minecraft.getMinecraft().player.posZ + face.zOffset
            .toDouble() / 2
        var y = block.getY() + 0.5
        if (Legit) y += 0.5
        val d1 = Minecraft.getMinecraft().player.posY + Minecraft.getMinecraft().player.getEyeHeight() - y
        val d3 = MathHelper.sqrt(x * x + z * z).toDouble()
        var yaw = (atan2(z, x) * 180.0 / Math.PI).toFloat() - 90.0f
        val pitch = (atan2(d1, d3) * 180.0 / Math.PI).toFloat()
        if (yaw < 0.0f) {
            yaw += 360f
        }
        return floatArrayOf(yaw, pitch)
    }
}
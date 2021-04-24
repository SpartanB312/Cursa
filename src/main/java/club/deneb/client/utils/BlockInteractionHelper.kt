package club.deneb.client.utils

import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import java.util.*

object BlockInteractionHelper {

    @JvmField
    val blackList = listOf(
        Blocks.ENDER_CHEST,
        Blocks.CHEST,
        Blocks.TRAPPED_CHEST,
        Blocks.CRAFTING_TABLE,
        Blocks.ANVIL,
        Blocks.BREWING_STAND,
        Blocks.HOPPER,
        Blocks.DROPPER,
        Blocks.DISPENSER,
        Blocks.TRAPDOOR
    )


    @JvmField
    val shulkerList = listOf(
        Blocks.WHITE_SHULKER_BOX,
        Blocks.ORANGE_SHULKER_BOX,
        Blocks.MAGENTA_SHULKER_BOX,
        Blocks.LIGHT_BLUE_SHULKER_BOX,
        Blocks.YELLOW_SHULKER_BOX,
        Blocks.LIME_SHULKER_BOX,
        Blocks.PINK_SHULKER_BOX,
        Blocks.GRAY_SHULKER_BOX,
        Blocks.SILVER_SHULKER_BOX,
        Blocks.CYAN_SHULKER_BOX,
        Blocks.PURPLE_SHULKER_BOX,
        Blocks.BLUE_SHULKER_BOX,
        Blocks.BROWN_SHULKER_BOX,
        Blocks.GREEN_SHULKER_BOX,
        Blocks.RED_SHULKER_BOX,
        Blocks.BLACK_SHULKER_BOX
    )

    @JvmStatic
    fun checkForNeighbours(blockPos: BlockPos): Boolean {
        if (!hasNeighbour(blockPos)) {
            for (side in EnumFacing.values()) {
                val neighbour = blockPos.offset(side)
                if (hasNeighbour(neighbour)) {
                    return true
                }
            }
            return false
        }
        return true
    }

    private fun hasNeighbour(blockPos: BlockPos): Boolean {
        for (side in EnumFacing.values()) {
            val neighbour = blockPos.offset(side)
            if (!Minecraft.getMinecraft().world.getBlockState(neighbour).material.isReplaceable) {
                return true
            }
        }
        return false
    }

    fun getSphere(loc: BlockPos, r: Float, h: Int, hollow: Boolean, sphere: Boolean, plus_y: Int): List<BlockPos> {
        val circleBlocks: MutableList<BlockPos> = ArrayList()
        val cx = loc.getX()
        val cy = loc.getY()
        val cz = loc.getZ()
        var x = cx - r.toInt()
        while (x <= cx + r) {
            var z = cz - r.toInt()
            while (z <= cz + r) {
                var y = if (sphere) cy - r.toInt() else cy
                while (y < if (sphere) cy.toFloat() + r else cy.toFloat() + h) {
                    val dist =
                        ((cx - x) * (cx - x) + (cz - z) * (cz - z) + if (sphere) (cy - y) * (cy - y) else 0).toDouble()
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        val l = BlockPos(x, y + plus_y, z)
                        circleBlocks.add(l)
                    }
                    y++
                }
                z++
            }
            x++
        }
        return circleBlocks
    }
}
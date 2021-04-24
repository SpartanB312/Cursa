package club.deneb.client.features.modules.render

import club.deneb.client.event.events.render.RenderEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.utils.ChatUtil
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.init.Blocks
import net.minecraft.pathfinding.PathFinder
import net.minecraft.pathfinding.PathNodeType
import net.minecraft.pathfinding.PathPoint
import net.minecraft.pathfinding.WalkNodeProcessor
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import org.lwjgl.opengl.GL11
import java.util.*

@Module.Info(
    name = "PathFind",
    description = "Automatically find path",
    category = Category.MISC
)
class PathFind : Module() {

    override fun onWorldRender(event: RenderEvent) {
        if (points.isEmpty()) return
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glDisable(GL11.GL_TEXTURE_2D)
        GL11.glDisable(GL11.GL_LIGHTING)
        GL11.glLineWidth(1.5f)
        GL11.glColor3f(1f, 1f, 1f)
        GlStateManager.disableDepth()
        GL11.glBegin(GL11.GL_LINES)
        val first = points[0]
        GL11.glVertex3d(
            first.x - mc.getRenderManager().renderPosX + .5,
            first.y - mc.getRenderManager().renderPosY,
            first.z - mc.getRenderManager().renderPosZ + .5
        )
        for (i in 0 until points.size - 1) {
            val pathPoint = points[i]
            GL11.glVertex3d(
                pathPoint.x - mc.getRenderManager().renderPosX + .5,
                pathPoint.y - mc.getRenderManager().renderPosY,
                pathPoint.z - mc.getRenderManager().renderPosZ + .5
            )
            if (i != points.size - 1) {
                GL11.glVertex3d(
                    pathPoint.x - mc.getRenderManager().renderPosX + .5,
                    pathPoint.y - mc.getRenderManager().renderPosY,
                    pathPoint.z - mc.getRenderManager().renderPosZ + .5
                )
            }
        }
        GL11.glEnd()
        GlStateManager.enableDepth()
    }

    override fun onTick() {
        val closest = points.stream().min(Comparator.comparing { pathPoint: PathPoint ->
            mc.player.getDistance(
                pathPoint.x.toDouble(),
                pathPoint.y.toDouble(),
                pathPoint.z.toDouble()
            )
        }).orElse(null)
            ?: return
        if (mc.player.getDistance(closest.x.toDouble(), closest.y.toDouble(), closest.z.toDouble()) > .8) return
        val iterator = points.iterator()
        while (iterator.hasNext()) {
            if (iterator.next() === closest) {
                iterator.remove()
                break
            }
            iterator.remove()
        }
        if (points.size <= 1 && to != null) {
            val b = createPath(to)
            val flag = points.size <= 4
            if (b && flag || flag) {    // The only points present are the starting and end point (or <=2 points in between). We've arrived!
                // Might also return true if we've hit a dead end
                points.clear()
                to = null
                if (b) ChatUtil.printChatMessage("Arrived!") else ChatUtil.printChatMessage("Can't go on: pathfinder has hit dead end")
            }
        }
    }

    @Suppress("DEPRECATION")
    private class AnchoredWalkNodeProcessor(var from: PathPoint) : WalkNodeProcessor() {
        override fun getStart(): PathPoint {
            return from
        }

        override fun getCanEnterDoors(): Boolean {
            return true
        }

        override fun getCanSwim(): Boolean {
            return true
        }

        override fun getPathNodeType(blockaccessIn: IBlockAccess, x: Int, y: Int, z: Int): PathNodeType {
            var pathnodetype = getPathNodeTypeRaw(blockaccessIn, x, y, z)
            if (pathnodetype == PathNodeType.OPEN && y >= 1) {
                val block = blockaccessIn.getBlockState(BlockPos(x, y - 1, z)).block
                val pathnodetype1 = getPathNodeTypeRaw(blockaccessIn, x, y - 1, z)
                pathnodetype =
                    if (pathnodetype1 != PathNodeType.WALKABLE && pathnodetype1 != PathNodeType.OPEN && pathnodetype1 != PathNodeType.LAVA) PathNodeType.WALKABLE else PathNodeType.OPEN
                if (pathnodetype1 == PathNodeType.DAMAGE_FIRE || block === Blocks.MAGMA) {
                    pathnodetype = PathNodeType.DAMAGE_FIRE
                }
                if (pathnodetype1 == PathNodeType.DAMAGE_CACTUS) {
                    pathnodetype = PathNodeType.DAMAGE_CACTUS
                }
            }
            pathnodetype = checkNeighborBlocks(blockaccessIn, x, y, z, pathnodetype)
            return pathnodetype
        }

        override fun getPathNodeTypeRaw(
            p_189553_1_: IBlockAccess,
            p_189553_2_: Int,
            p_189553_3_: Int,
            p_189553_4_: Int
        ): PathNodeType {
            val blockpos = BlockPos(p_189553_2_, p_189553_3_, p_189553_4_)
            val iblockstate = p_189553_1_.getBlockState(blockpos)
            val block = iblockstate.block
            val material = iblockstate.material
            val type = block.getAiPathNodeType(iblockstate, p_189553_1_, blockpos)
            if (type != null) return type
            return if (material === Material.AIR) {
                PathNodeType.OPEN
            } else if (block !== Blocks.TRAPDOOR && block !== Blocks.IRON_TRAPDOOR && block !== Blocks.WATERLILY) {
                if (block === Blocks.FIRE) {
                    PathNodeType.DAMAGE_FIRE
                } else if (block === Blocks.CACTUS) {
                    PathNodeType.DAMAGE_CACTUS
                } else if (block is BlockDoor && material === Material.WOOD && !iblockstate.getValue(
                        BlockDoor.OPEN
                    )
                ) {
                    PathNodeType.DOOR_WOOD_CLOSED
                } else if (block is BlockDoor && material === Material.IRON && !iblockstate.getValue(
                        BlockDoor.OPEN
                    )
                ) {
                    PathNodeType.DOOR_IRON_CLOSED
                } else if (block is BlockDoor && iblockstate.getValue(BlockDoor.OPEN)) {
                    PathNodeType.DOOR_OPEN
                } else if (block is BlockRailBase) {
                    PathNodeType.RAIL
                } else if (block !is BlockFence && block !is BlockWall && (block !is BlockFenceGate || iblockstate.getValue(
                        BlockFenceGate.OPEN
                    ))
                ) {
                    if (material === Material.WATER) {
                        PathNodeType.WALKABLE
                    } else if (material === Material.LAVA) {
                        PathNodeType.LAVA
                    } else {
                        if (block.isPassable(p_189553_1_, blockpos)) PathNodeType.OPEN else PathNodeType.BLOCKED
                    }
                } else {
                    PathNodeType.FENCE
                }
            } else {
                PathNodeType.TRAPDOOR
            }
        }
    }

    companion object {
        var points = ArrayList<PathPoint>()
        var to: PathPoint? = null
        fun createPath(end: PathPoint?): Boolean {
            to = end
            val walkNodeProcessor: WalkNodeProcessor = AnchoredWalkNodeProcessor(
                PathPoint(
                    mc.player.posX.toInt(),
                    mc.player.posY.toInt(),
                    mc.player.posZ.toInt()
                )
            )
            val zombie = EntityZombie(mc.world)
            zombie.setPathPriority(PathNodeType.WATER, 16f)
            zombie.posX = mc.player.posX
            zombie.posY = mc.player.posY
            zombie.posZ = mc.player.posZ
            val finder = PathFinder(walkNodeProcessor)
            val path = finder.findPath(mc.world, zombie, BlockPos(end!!.x, end.y, end.z), Float.MAX_VALUE)
            zombie.setPathPriority(PathNodeType.WATER, 0f)
            if (path == null) {
                ChatUtil.printChatMessage("Failed to create path!")
                return false
            }
            points = ArrayList(listOf(*path.points))
            return points[points.size - 1].distanceTo(end) <= 1 // Return whether or not the last path location is our end destination
        }
    }
}
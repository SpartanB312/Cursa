package club.deneb.client.features.modules.render

import club.deneb.client.event.events.render.RenderEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.utils.DenebTessellator
import club.deneb.client.utils.GeometryMasks
import club.deneb.client.utils.Wrapper
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.entity.item.EntityItemFrame
import net.minecraft.entity.item.EntityMinecartChest
import net.minecraft.item.ItemShulkerBox
import net.minecraft.tileentity.*
import net.minecraft.util.math.BlockPos
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*

@Module.Info(
    name = "StorageESP",
    description = "Draws nice little lines around storage items",
    category = Category.RENDER
)
class StorageESP : Module() {

    private fun getTileEntityColor(tileEntity: TileEntity): Int {
        return if (tileEntity is TileEntityChest || tileEntity is TileEntityDispenser || tileEntity is TileEntityShulkerBox) Color(255,128,0,255).rgb else if (tileEntity is TileEntityEnderChest) Color(163,73,163,255).rgb else if (tileEntity is TileEntityFurnace) Color(127,127,127,255).rgb else if (tileEntity is TileEntityHopper) Color(64,0,0,255).rgb else -1
    }

    private fun getEntityColor(entity: Entity): Int {
        return if (entity is EntityMinecartChest) Color(255,128,0,255).rgb else if (entity is EntityItemFrame &&
            entity.displayedItem.getItem() is ItemShulkerBox
        ) Color(255,255,0,255).rgb else -1
    }

    override fun onWorldRender(event: RenderEvent) {
        val a = ArrayList<Triplet<BlockPos, Int, Int>>()
        GlStateManager.pushMatrix()
        for (tileEntity in Wrapper.world.loadedTileEntityList) {
            val pos = tileEntity.pos
            val color = getTileEntityColor(tileEntity)
            var side = GeometryMasks.Quad.ALL
            if (tileEntity is TileEntityChest) {
                // Leave only the colliding face and then flip the bits (~) to have ALL but that face
                if (tileEntity.adjacentChestZNeg != null) side = (side and GeometryMasks.Quad.NORTH).inv()
                if (tileEntity.adjacentChestXPos != null) side = (side and GeometryMasks.Quad.EAST).inv()
                if (tileEntity.adjacentChestZPos != null) side = (side and GeometryMasks.Quad.SOUTH).inv()
                if (tileEntity.adjacentChestXNeg != null) side = (side and GeometryMasks.Quad.WEST).inv()
            }
            if (color != -1) a.add(
                Triplet(
                    pos,
                    color,
                    side
                )
            ) //GeometryTessellator.drawCuboid(event.getBuffer(), pos, GeometryMasks.Line.ALL, color);
        }
        for (entity in Wrapper.world.loadedEntityList) {
            val pos = entity.position
            val color = getEntityColor(entity)
            if (color != -1) a.add(
                Triplet(
                    if (entity is EntityItemFrame) pos.add(0, -1, 0) else pos,
                    color,
                    GeometryMasks.Quad.ALL
                )
            ) //GeometryTessellator.drawCuboid(event.getBuffer(), entity instanceof EntityItemFrame ? pos.add(0, -1, 0) : pos, GeometryMasks.Line.ALL, color);
        }
        DenebTessellator.prepare(GL11.GL_QUADS)
        for (pair in a) DenebTessellator.drawBox(pair.first, changeAlpha(pair.second), pair.third)
        DenebTessellator.release()
        GlStateManager.popMatrix()
        GlStateManager.enableTexture2D()
    }

    private fun changeAlpha(origColorIn: Int): Int {
        var origColor = origColorIn
        origColor = origColor and 0x00ffffff
        return 100 shl 24 or origColor
    }

    class Triplet<T, U, V>(val first: T, val second: U, val third: V)
}
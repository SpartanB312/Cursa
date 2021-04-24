package club.deneb.client.utils;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class DenebTessellator extends Tessellator {
    public static DenebTessellator INSTANCE = new DenebTessellator();

    public static ICamera camera = new Frustum();

    public DenebTessellator() {
        super(2097152);
    }

    public static void prepare(int mode) {
        DenebTessellator.prepareGL();
        DenebTessellator.begin(mode);
    }

    public static void drawEntityBox(AxisAlignedBB bb, int r, int g, int b, int a, int sides) {
        DenebTessellator.drawEntityBox(INSTANCE.getBuffer(), bb, r, g, b, a, sides);
    }

    public static void drawTracer(Entity entity, float red, float green, float blue, float alpha, float width, float height) {
        double renderPosX = Wrapper.minecraft.getRenderManager().viewerPosX;
        double renderPosY = Wrapper.minecraft.getRenderManager().viewerPosY;
        double renderPosZ = Wrapper.minecraft.getRenderManager().viewerPosZ;
        double xPos = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX)) - renderPosX;
        double yPos = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY))  + (entity.height * height) - renderPosY;
        double zPos = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ)) - renderPosZ;

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(width);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glColor4f(red, green, blue, alpha);

        Vec3d eyes = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(Wrapper.minecraft.player.rotationPitch)).rotateYaw(-(float) Math.toRadians(Wrapper.minecraft.player.rotationYaw));

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3d(eyes.x, Wrapper.minecraft.player.getEyeHeight() + eyes.y, eyes.z);
        GL11.glVertex3d(xPos, yPos, zPos);
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);

        GlStateManager.resetColor();
    }

    public static void drawFullBox(AxisAlignedBB bb, float width, int argb) {
        int a = argb >>> 24 & 255;
        int r = argb >>> 16 & 255;
        int g = argb >>> 8 & 255;
        int b = argb & 255;
        DenebTessellator.drawFullBox(bb, width, r, g, b, a);
    }




    public static void prepareGL() {
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(1.5f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }

    public static void releaseGL() {
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void begin(int mode) {
        INSTANCE.getBuffer().begin(mode, DefaultVertexFormats.POSITION_COLOR);
    }

    public static void release() {
        DenebTessellator.render();
        DenebTessellator.releaseGL();
    }

    public static void render() {
        INSTANCE.draw();
    }

    public static void drawBox(BlockPos blockPos, int argb, int sides) {
        int a = argb >>> 24 & 255;
        int r = argb >>> 16 & 255;
        int g = argb >>> 8 & 255;
        int b = argb & 255;
        DenebTessellator.drawBox(blockPos, r, g, b, a, sides);
    }


    public static void drawEntityBox(BufferBuilder buffer, AxisAlignedBB bb, int r, int g, int b, int a, int sides) {
        if ((sides & GeometryMasks.Quad.DOWN) != 0) {
            buffer.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
        }

        if ((sides & GeometryMasks.Quad.UP) != 0) {
            buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        }

        if ((sides & GeometryMasks.Quad.NORTH) != 0) {
            buffer.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
        }

        if ((sides & GeometryMasks.Quad.SOUTH) != 0) {
            buffer.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        }

        if ((sides & GeometryMasks.Quad.WEST) != 0) {
            buffer.pos(bb.minX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.minX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.minX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.minX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
        }

        if ((sides & GeometryMasks.Quad.EAST) != 0) {
            buffer.pos(bb.maxX, bb.minY, bb.maxZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.maxX, bb.minY, bb.minZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.maxX, bb.maxY, bb.minZ).color(r, g, b, a).endVertex();
            buffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(r, g, b, a).endVertex();
        }
    }


    public static void drawBox(BlockPos blockPos, int r, int g, int b, int a, int sides) {
        DenebTessellator.drawBox(INSTANCE.getBuffer(), blockPos.x, blockPos.y, blockPos.z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }


    public static void drawBox(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
        if ((sides & 1) != 0) {
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
        }
        if ((sides & 2) != 0) {
            buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
        }
        if ((sides & 4) != 0) {
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
        }
        if ((sides & 8) != 0) {
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
        }
        if ((sides & 16) != 0) {
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
        }
        if ((sides & 32) != 0) {
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
        }
    }
    public static void drawFace(BlockPos blockPos, int r, int g, int b, int a, int sides) {
        DenebTessellator.drawFace(INSTANCE.getBuffer(), blockPos.x, blockPos.y, blockPos.z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }

    public static void drawFace(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
        if ((sides & 1) != 0) {
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
        }
    }

    public static void drawFaceOutline(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
        if ((sides & 1) != 0) {
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, (double)z + 0.02).color(r, g, b, a).endVertex();
            buffer.pos(x, y, (double)z + 0.02).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos((double)x + 0.02, y, z).color(r, g, b, a).endVertex();
            buffer.pos((double)x + 0.02, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, (double)(z + d) - 0.02).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, (double)(z + d) - 0.02).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w) - 0.02, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w) - 0.02, y, z).color(r, g, b, a).endVertex();
        }
    }

    public static void drawFullBox(BlockPos pos, float width, int argb) {
        int a = argb >>> 24 & 255;
        int r = argb >>> 16 & 255;
        int g = argb >>> 8 & 255;
        int b = argb & 255;
        DenebTessellator.drawFullBox(pos, width, r, g, b, a);
    }
    public static void drawFullBox(BlockPos pos, float width, int red, int green, int blue, int alpha) {
        drawBoundingFullBox(getBoundingFromPos(pos), red, green, blue, alpha);
        drawBoundingBox(getBoundingFromPos(pos), width, red, green, blue, 255);
    }

    public static AxisAlignedBB getBoundingFromPos(BlockPos pos){
        IBlockState iBlockState = Wrapper.minecraft.world.getBlockState(pos);
        Vec3d interp = interpolateEntity(Wrapper.minecraft.player, Wrapper.minecraft.getRenderPartialTicks());
        return iBlockState.getSelectedBoundingBox(Wrapper.minecraft.world, pos).expand(0.0020000000949949026D, 0.0020000000949949026D, 0.0020000000949949026D).offset(-interp.x, -interp.y, -interp.z);
    }

    public static Vec3d interpolateEntity(final Entity entity, final float time) {
        return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
    }

    public static Vec3d interpolateEntityClose(final Entity entity, float renderPartialTicks) {
        return new Vec3d(
                calculateDistanceWithPartialTicks(entity.posX, entity.lastTickPosX, renderPartialTicks) - Wrapper.minecraft.getRenderManager().renderPosX,
                calculateDistanceWithPartialTicks(entity.posY, entity.lastTickPosY, renderPartialTicks) - Wrapper.minecraft.getRenderManager().renderPosY,
                calculateDistanceWithPartialTicks(entity.posZ, entity.lastTickPosZ, renderPartialTicks) - Wrapper.minecraft.getRenderManager().renderPosZ);
    }

    public static double calculateDistanceWithPartialTicks(final double n, final double n2, final float renderPartialTicks) {
        return n2 + (n - n2) * Wrapper.minecraft.getRenderPartialTicks();
    }

    public static void drawFullBox(AxisAlignedBB bb, float width, int red, int green, int blue, int alpha) {
        drawBoundingFullBox(bb, red, green, blue, alpha);
        drawBoundingBox(bb, width, red, green, blue, 255);
    }

    public static void drawBoundingBox(AxisAlignedBB bb, float width, int argb) {
        int a = argb >>> 24 & 255;
        int r = argb >>> 16 & 255;
        int g = argb >>> 8 & 255;
        int b = argb & 255;
        DenebTessellator.drawBoundingBox(bb, width, r, g, b, a);
    }

    public static void drawBoundingBox(BlockPos bb, float width, int argb) {
        int a = argb >>> 24 & 255;
        int r = argb >>> 16 & 255;
        int g = argb >>> 8 & 255;
        int b = argb & 255;
        DenebTessellator.drawBoundingBox(bb, width, r, g, b, a);
    }

    public static void drawBoundingBox(BlockPos pos, float width, int red, int green, int blue, int alpha){
        drawBoundingBox(getBoundingFromPos(pos), width, red, green, blue, alpha);
    }

    public static void drawBoundingBox(AxisAlignedBB bb, float width, int red, int green, int blue, int alpha){
        glLineWidth(width);
        GL11.glEnable(GL_LINE_SMOOTH);
        GlStateManager.color(red / 255f, green / 255f, blue / 255f, alpha / 255f);
        drawBoundingBox(bb);
        GlStateManager.color(1, 1, 1, 1);
        GL11.glDisable(GL_LINE_SMOOTH);
    }


    public static void drawBoxOutline(BlockPos blockPos, int r, int g, int b, int a, int sides) {
        DenebTessellator.drawBoxOutline(INSTANCE.getBuffer(), blockPos.x, blockPos.y, blockPos.z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }

    public static void drawBoxOutline(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
        if ((sides & 1) != 0) {
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, (double)z + 0.02).color(r, g, b, a).endVertex();
            buffer.pos(x, y, (double)z + 0.02).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos((double)x + 0.02, y, z).color(r, g, b, a).endVertex();
            buffer.pos((double)x + 0.02, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, (double)(z + d) - 0.02).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y, (double)(z + d) - 0.02).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w) - 0.02, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos((double)(x + w) - 0.02, y, z).color(r, g, b, a).endVertex();
        }
    }

    public static void drawBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawFilledBox(final AxisAlignedBB axisAlignedBB) {
        final Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();

        vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();

        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();

        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();

        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();

        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();

        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ).endVertex();
        vertexbuffer.pos(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ).endVertex();
        tessellator.draw();
    }

    public static void drawLines(BlockPos blockPos, int r, int g, int b, int a, int sides) {
        DenebTessellator.drawLines(INSTANCE.getBuffer(), blockPos.x, blockPos.y, blockPos.z, 1.0f, 1.0f, 1.0f, r, g, b, a, sides);
    }

    public static void drawLines(BufferBuilder buffer, float x, float y, float z, float w, float h, float d, int r, int g, int b, int a, int sides) {
        if ((sides & 17) != 0) {
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
        }
        if ((sides & 18) != 0) {
            buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
        }
        if ((sides & 33) != 0) {
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
        }
        if ((sides & 34) != 0) {
            buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
        }
        if ((sides & 5) != 0) {
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
        }
        if ((sides & 6) != 0) {
            buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
        }
        if ((sides & 9) != 0) {
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
        }
        if ((sides & 10) != 0) {
            buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
        }
        if ((sides & 20) != 0) {
            buffer.pos(x, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z).color(r, g, b, a).endVertex();
        }
        if ((sides & 36) != 0) {
            buffer.pos(x + w, y, z).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z).color(r, g, b, a).endVertex();
        }
        if ((sides & 24) != 0) {
            buffer.pos(x, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x, y + h, z + d).color(r, g, b, a).endVertex();
        }
        if ((sides & 40) != 0) {
            buffer.pos(x + w, y, z + d).color(r, g, b, a).endVertex();
            buffer.pos(x + w, y + h, z + d).color(r, g, b, a).endVertex();
        }
    }

    public static void drawESPOutline(final AxisAlignedBB bb, final float width, final int red, final int green, final int blue, final int alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_POINTS, GL11.GL_LINES);
        GlStateManager.disableTexture2D();
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.depthMask(false);
        GlStateManager.glLineWidth(width);
        final BufferBuilder bufferbuilder = INSTANCE.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        render();
        GlStateManager.depthMask(true);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBoundingFullBox(AxisAlignedBB bb, int red, int green, int blue, int alpha) {
        GlStateManager.color(red / 255f, green / 255f, blue / 255f, alpha / 255f);
        drawFilledBox(bb);
        GlStateManager.color(1, 1, 1, 1);
    }
    public static void drawBoundingFullBox(BlockPos pos, int red, int green, int blue, int alpha) {
        drawBoundingFullBox(getBoundingFromPos(pos), red, green, blue, alpha);
    }

    public static void drawBoundingBoxFace(AxisAlignedBB bb, float width, int red, int green, int blue, int alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_POINTS, GL11.GL_LINES);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(width);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawBoundingBoxBottom(final AxisAlignedBB bb, final float width, final int red, final int green, final int blue, final int alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_POINTS, GL11.GL_LINES);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GlStateManager.glLineWidth(width);
        //final Tessellator tessellator = Tessellator.getInstance();
        //final BufferBuilder bufferbuilder = tessellator.getBuffer();
        final BufferBuilder bufferbuilder = INSTANCE.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        //tessellator.draw();
        render();
        GlStateManager.depthMask(true);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
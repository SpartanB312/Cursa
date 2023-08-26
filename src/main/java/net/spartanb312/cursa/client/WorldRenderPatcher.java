package net.spartanb312.cursa.client;

import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.event.events.render.RenderEvent;
import net.spartanb312.cursa.event.events.render.RenderWorldEvent;
import net.spartanb312.cursa.notification.NotificationManager;
import net.spartanb312.cursa.graphics.RenderUtils3D;
import net.spartanb312.cursa.utils.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

public class WorldRenderPatcher {

    public static WorldRenderPatcher INSTANCE = new WorldRenderPatcher();

    public void patch(RenderWorldEvent event) {
        Minecraft.getMinecraft().profiler.startSection("cursa");

        Minecraft.getMinecraft().profiler.startSection("setup");
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();

        GlStateManager.glLineWidth(1f);
        Vec3d renderPos = getInterpolatedPos(Objects.requireNonNull(Minecraft.getMinecraft().getRenderViewEntity()), event.getPartialTicks());

        RenderEvent e = new RenderEvent(RenderUtils3D.INSTANCE, renderPos);
        e.resetTranslation();
        Minecraft.getMinecraft().profiler.endSection();

        Cursa.MODULE_BUS.getModules().forEach(it -> {
            try {
                it.onRenderWorld(e);
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running onRenderWorld!");
                exception.printStackTrace();
            }
        });

        Minecraft.getMinecraft().profiler.startSection("release");
        GlStateManager.glLineWidth(1f);

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        RenderUtils3D.releaseGL();
        Minecraft.getMinecraft().profiler.endSection();
    }

    public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(EntityUtil.getInterpolatedAmount(entity, ticks));
    }

}

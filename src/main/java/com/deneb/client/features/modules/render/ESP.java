package com.deneb.client.features.modules.render;

import com.deneb.client.event.events.render.RenderEvent;
import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.utils.EntityUtil;
import com.deneb.client.utils.Wrapper;
import com.deneb.client.value.BValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by 086 on 14/12/2017.
 */
@Module.Info(name = "ESP", category = Category.RENDER)
public class ESP extends Module {

    BValue players = setting("Players", true);
    BValue animals = setting("Animals", false);
    BValue mobs = setting("Mobs", false);


    @Override
    public void onWorldRender(RenderEvent event) {
        if (Wrapper.getMinecraft().getRenderManager().options == null) return;
        boolean isThirdPersonFrontal = Wrapper.getMinecraft().getRenderManager().options.thirdPersonView == 2;
        float viewerYaw = Wrapper.getMinecraft().getRenderManager().playerViewY;

        mc.world.loadedEntityList.stream()
                .filter(EntityUtil::isLiving)
                .filter(entity -> mc.player != entity)
                .map(entity -> (EntityLivingBase) entity)
                .filter(entityLivingBase -> !entityLivingBase.isDead)
                .filter(entity -> (players.getValue() && entity instanceof EntityPlayer) || (EntityUtil.isPassive(entity) ? animals.getValue() : mobs.getValue()))
                .forEach(e -> {
                    GlStateManager.pushMatrix();
                    Vec3d pos = EntityUtil.getInterpolatedPos(e, event.getPartialTicks());
                    GlStateManager.translate(pos.x - mc.getRenderManager().renderPosX, pos.y - mc.getRenderManager().renderPosY, pos.z - mc.getRenderManager().renderPosZ);
                    GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate((float) (isThirdPersonFrontal ? -1 : 1), 1.0F, 0.0F, 0.0F);
                    GlStateManager.disableLighting();
                    GlStateManager.depthMask(false);

                    GlStateManager.disableDepth();

                    GlStateManager.enableBlend();
                    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

                    if (e instanceof EntityPlayer) glColor3f(1, 1, 1);
                    else if (EntityUtil.isPassive(e)) glColor3f(0.11f, 0.9f, 0.11f);
                    else glColor3f(0.9f, .1f, .1f);

                    GlStateManager.disableTexture2D();
                    glLineWidth(2f);
                    glEnable(GL_LINE_SMOOTH);
                    glBegin(GL_LINE_LOOP);
                    {
                        glVertex2d(-e.width / 2, 0);
                        glVertex2d(-e.width / 2, e.height);
                        glVertex2d(e.width / 2, e.height);
                        glVertex2d(e.width / 2, 0);
                    }
                    glEnd();

                    GlStateManager.popMatrix();
                });
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.disableDepth();
        GlStateManager.enableCull();
        GlStateManager.glLineWidth(1);
        glColor3f(1, 1, 1);
    }
}

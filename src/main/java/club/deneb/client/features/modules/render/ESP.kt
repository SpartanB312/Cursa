package club.deneb.client.features.modules.render

import club.deneb.client.event.events.render.RenderEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.utils.EntityUtil
import club.deneb.client.utils.Wrapper
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.opengl.GL11

@Module.Info(
    name = "ESP",
    description = "Draw a box on the entities around you",
    category = Category.RENDER
)
class ESP : Module() {

    private val players = setting("Players", true)
    private val animals = setting("Animals", false)
    private val mobs = setting("Mobs", false)

    override fun onWorldRender(event: RenderEvent) {
        if (Wrapper.getMinecraft().getRenderManager().options == null) return
        val isThirdPersonFrontal = Wrapper.getMinecraft().getRenderManager().options.thirdPersonView == 2
        val viewerYaw = Wrapper.getMinecraft().getRenderManager().playerViewY
        mc.world.loadedEntityList.stream()
            .filter { e: Entity -> EntityUtil.isLiving(e) }
            .filter { entity: Entity -> mc.player !== entity }
            .map { entity: Entity -> entity as EntityLivingBase }
            .filter { entityLivingBase: EntityLivingBase -> !entityLivingBase.isDead }
            .filter { entity: EntityLivingBase? ->
                players.value && entity is EntityPlayer || if (EntityUtil.isPassive(
                        entity
                    )
                ) animals.value else mobs.value
            }
            .forEach { e: EntityLivingBase ->
                GlStateManager.pushMatrix()
                val pos = EntityUtil.getInterpolatedPos(e, event.partialTicks)
                GlStateManager.translate(
                    pos.x - mc.getRenderManager().renderPosX,
                    pos.y - mc.getRenderManager().renderPosY,
                    pos.z - mc.getRenderManager().renderPosZ
                )
                GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f)
                GlStateManager.rotate(-viewerYaw, 0.0f, 1.0f, 0.0f)
                GlStateManager.rotate((if (isThirdPersonFrontal) -1 else 1).toFloat(), 1.0f, 0.0f, 0.0f)
                GlStateManager.disableLighting()
                GlStateManager.depthMask(false)
                GlStateManager.disableDepth()
                GlStateManager.enableBlend()
                GlStateManager.tryBlendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
                )
                when {
                    e is EntityPlayer -> GL11.glColor3f(
                        1f,
                        1f,
                        1f
                    )
                    EntityUtil.isPassive(e) -> GL11.glColor3f(0.11f, 0.9f, 0.11f)
                    else -> GL11.glColor3f(
                        0.9f,
                        .1f,
                        .1f
                    )
                }
                GlStateManager.disableTexture2D()
                GL11.glLineWidth(2f)
                GL11.glEnable(GL11.GL_LINE_SMOOTH)
                GL11.glBegin(GL11.GL_LINE_LOOP)
                run {
                    GL11.glVertex2d((-e.width / 2).toDouble(), 0.0)
                    GL11.glVertex2d((-e.width / 2).toDouble(), e.height.toDouble())
                    GL11.glVertex2d((e.width / 2).toDouble(), e.height.toDouble())
                    GL11.glVertex2d((e.width / 2).toDouble(), 0.0)
                }
                GL11.glEnd()
                GlStateManager.popMatrix()
            }
        GlStateManager.enableDepth()
        GlStateManager.depthMask(true)
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)
        GlStateManager.shadeModel(GL11.GL_SMOOTH)
        GlStateManager.disableDepth()
        GlStateManager.enableCull()
        GlStateManager.glLineWidth(1f)
        GL11.glColor3f(1f, 1f, 1f)
    }
}
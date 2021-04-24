package club.deneb.client.features.modules.render

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

@Module.Info(
    name = "ArmourHUD",
    description = "Render the armour you wearing",
    category = Category.RENDER
)
class ArmourHUD : Module() {

    private val damage = setting("Damage", false)

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Post?) {
        GlStateManager.enableTexture2D()
        val resolution = ScaledResolution(mc)
        val i = resolution.scaledWidth / 2
        var iteration = 0
        val y = resolution.scaledHeight - 55 - if (mc.player.isInWater) 10 else 0
        for (`is` in mc.player.inventory.armorInventory) {
            iteration++
            if (`is`.isEmpty()) continue
            val x = i - 90 + (9 - iteration) * 20 + 2
            GlStateManager.enableDepth()
            itemRender.zLevel = 200f
            itemRender.renderItemAndEffectIntoGUI(`is`, x, y)
            itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, `is`, x, y, "")
            itemRender.zLevel = 0f
            GlStateManager.enableTexture2D()
            GlStateManager.disableLighting()
            GlStateManager.disableDepth()
            val s = if (`is`.count > 1) `is`.count.toString() + "" else ""
            mc.fontRenderer.drawStringWithShadow(
                s,
                (x + 19 - 2 - mc.fontRenderer.getStringWidth(s)).toFloat(),
                (y + 9).toFloat(),
                0xffffff
            )
            if (damage.value) {
                val green = (`is`.maxDamage.toFloat() - `is`.getItemDamage().toFloat()) / `is`.maxDamage
                    .toFloat()
                val red = 1 - green
                val dmg = 100 - (red * 100).toInt()
                mc.fontRenderer.drawStringWithShadow(
                    dmg.toString() + "",
                    (x + 8 - mc.fontRenderer.getStringWidth(dmg.toString() + "") / 2).toFloat(),
                    (y - 11).toFloat(),
                    Color((red * 255).toInt(), (green * 255).toInt(), 0).rgb
                )
            }
        }
        GlStateManager.enableDepth()
        GlStateManager.disableLighting()
    }

    companion object {
        private val itemRender = Minecraft.getMinecraft().getRenderItem()
    }
}
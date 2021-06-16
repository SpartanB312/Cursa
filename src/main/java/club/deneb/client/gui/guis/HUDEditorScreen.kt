package club.deneb.client.gui.guis

import club.deneb.client.Deneb
import club.deneb.client.client.GuiManager
import club.deneb.client.client.GuiManager.background
import club.deneb.client.client.GuiManager.isParticle
import club.deneb.client.features.ModuleManager
import club.deneb.client.utils.particles.ParticleSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.util.ResourceLocation
import java.awt.Color

/**
 * Created by B_312 on 01/10/21
 */
class HUDEditorScreen : GuiScreen() {

    private val particleSystem = ParticleSystem(100)

    override fun doesGuiPauseGame(): Boolean {
        return false
    }

    override fun initGui() {
        if (background == GuiManager.Background.Blur || background == GuiManager.Background.Both) {
            if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null) {
                Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup()
            }
            Minecraft.getMinecraft().entityRenderer.loadShader(ResourceLocation("shaders/post/blur.json"))
        }
    }

    override fun onGuiClosed() {
        if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null) {
            Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup()
        }
        if (ModuleManager.getModuleByName("HUDEditor").isEnabled) {
            ModuleManager.getModuleByName("HUDEditor").disable()
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (background == GuiManager.Background.Shadow || background == GuiManager.Background.Both) {
            drawDefaultBackground()
        }
        if (mc.player == null) drawRect(0, 0, 9999, 9999, Color(0, 0, 0, 255).rgb)
        if (isParticle) {
            particleSystem.tick(10)
            particleSystem.render()
        }
        Deneb.getINSTANCE().hudEditor.drawScreen(mouseX, mouseY, partialTicks)
    }

    public override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        Deneb.getINSTANCE().hudEditor.mouseClicked(mouseX, mouseY, mouseButton)
    }

    public override fun keyTyped(typedChar: Char, keyCode: Int) {
        Deneb.getINSTANCE().hudEditor.keyTyped(typedChar, keyCode)
    }

    public override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        Deneb.getINSTANCE().hudEditor.mouseReleased(mouseX, mouseY, state)
    }
}
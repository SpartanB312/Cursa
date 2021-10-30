package net.spartanb312.cursa.gui;

import net.spartanb312.cursa.client.FontManager;
import net.spartanb312.cursa.client.GUIManager;
import net.spartanb312.cursa.client.ModuleManager;
import net.spartanb312.cursa.gui.renderers.ClickGUIRenderer;
import net.spartanb312.cursa.module.modules.client.ClickGUI;
import net.spartanb312.cursa.module.modules.client.GUISetting;
import net.spartanb312.cursa.utils.graphics.RenderUtils2D;
import net.spartanb312.cursa.utils.math.Pair;
import net.spartanb312.cursa.utils.math.Vec2I;
import net.spartanb312.cursa.utils.graphics.particle.ParticleSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

@SuppressWarnings("ALL")
public class CursaClickGUI extends GuiScreen {

    public static Pair<String, Vec2I> description = null;
    public static int white = new Color(255, 255, 255, 255).getRGB();

    private final ParticleSystem particleSystem = new ParticleSystem(100);

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        if (GUIManager.getBackground().equals(GUISetting.Background.Blur) || GUIManager.getBackground().equals(GUISetting.Background.Both)) {
            if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null)
                Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
            Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }
    }


    @Override
    public void onGuiClosed() {
        if (Minecraft.getMinecraft().entityRenderer.getShaderGroup() != null)
            Minecraft.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
        if (ModuleManager.getModule(ClickGUI.class).isEnabled()) {
            ModuleManager.getModule(ClickGUI.class).disable();
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (GUIManager.getBackground().equals(GUISetting.Background.Shadow) || GUIManager.getBackground().equals(GUISetting.Background.Both)) {
            drawDefaultBackground();
        }
        description = null;
        if (GUIManager.isParticle()) {
            particleSystem.tick(10);
            particleSystem.render();
        }
        ClickGUIRenderer.instance.drawScreen(mouseX, mouseY, partialTicks);
        if (description != null) {
            RenderUtils2D.drawRect(description.b.x + 10, description.b.y, description.b.x + 12 + FontManager.getWidth(description.a), description.b.y + FontManager.getHeight() + 4, 0x85000000);
            RenderUtils2D.drawRectOutline(description.b.x + 10, description.b.y, description.b.x + 12 + FontManager.getWidth(description.a), description.b.y + FontManager.getHeight() + 4, GUIManager.getColor4I());
            FontManager.draw(description.a, description.b.x + 11, description.b.y + 4, white);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        ClickGUIRenderer.instance.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        ClickGUIRenderer.instance.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        ClickGUIRenderer.instance.mouseReleased(mouseX, mouseY, state);
    }
}

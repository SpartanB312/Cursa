package club.deneb.client.gui.guis;

import club.deneb.client.client.GuiManager;
import club.deneb.client.features.ModuleManager;
import club.deneb.client.Deneb;
import club.deneb.client.utils.Wrapper;
import club.deneb.client.utils.particles.ParticleSystem;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.awt.*;


/**
 * Created by B_312 on 01/10/21
 */
public class HUDEditorScreen extends GuiScreen {

    private final ParticleSystem particleSystem = new ParticleSystem(100);

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {

        if (GuiManager.INSTANCE.getBackground().equals(GuiManager.Background.Blur) || GuiManager.INSTANCE.getBackground().equals(GuiManager.Background.Both)) {
            if(Wrapper.getMinecraft().entityRenderer.getShaderGroup() != null) {
                Wrapper.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
            }
            Wrapper.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
        }

    }


    @Override
    public void onGuiClosed() {
        if(Wrapper.getMinecraft().entityRenderer.getShaderGroup() != null) {
            Wrapper.getMinecraft().entityRenderer.getShaderGroup().deleteShaderGroup();
        }
        if (ModuleManager.getModuleByName("HUDEditor").isEnabled()) {
            ModuleManager.getModuleByName("HUDEditor").disable();
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (GuiManager.INSTANCE.getBackground().equals(GuiManager.Background.Shadow) || GuiManager.INSTANCE.getBackground().equals(GuiManager.Background.Both)) {
            drawDefaultBackground();
        }
        if(mc.player == null) Gui.drawRect(0,0,9999,9999,new Color(0,0,0,255).getRGB());
        if (GuiManager.INSTANCE.isParticle()) {
            particleSystem.tick(10);
            particleSystem.render();
        }

        Deneb.getINSTANCE().getHudEditor().drawScreen( mouseX,  mouseY,  partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        Deneb.getINSTANCE().getHudEditor(). mouseClicked( mouseX,  mouseY,  mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        Deneb.getINSTANCE().getHudEditor().keyTyped( typedChar,  keyCode);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        Deneb.getINSTANCE().getHudEditor().mouseReleased( mouseX,  mouseY,  state);
    }
}

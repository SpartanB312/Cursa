package net.spartanb312.cursa.hud.huds;

import net.spartanb312.cursa.graphics.FontRenderers;
import net.spartanb312.cursa.client.GUIManager;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.engine.AsyncRenderer;
import net.spartanb312.cursa.graphics.font.WordArt;
import net.spartanb312.cursa.hud.HUDModule;
import net.spartanb312.cursa.module.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

@ModuleInfo(name = "Welcomer", category = Category.HUD)
public class Welcomer extends HUDModule {

    public Welcomer() {
        asyncRenderer = new AsyncRenderer() {
            @Override
            public void onUpdate(ScaledResolution resolution, int mouseX, int mouseY) {
                String text = "Welcome " + Minecraft.getMinecraft().player.getName() + "!Have a nice day :)";
                drawAsyncStringWithShadow(text, x, y, GUIManager.isRainbow() ? WordArt.rainbow(0) : GUIManager.getColor3I());
                width = FontRenderers.getWidth(text);
                height = FontRenderers.getHeight();
            }
        };
    }

    @Override
    public void onHUDRender(ScaledResolution resolution) {
        asyncRenderer.onRender();
    }

}

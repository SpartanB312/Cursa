package net.spartanb312.cursa.hud.huds;

import net.spartanb312.cursa.client.FontManager;
import net.spartanb312.cursa.client.GUIManager;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.engine.AsyncRenderer;
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
                drawAsyncString(text, x, y, GUIManager.getColor3I());
                width = FontManager.getWidth(text);
                height = FontManager.getHeight();
            }
        };
    }

    @Override
    public void onHUDRender(ScaledResolution resolution) {
        asyncRenderer.onRender();
    }

}

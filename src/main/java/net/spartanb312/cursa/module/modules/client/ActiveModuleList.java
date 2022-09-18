package net.spartanb312.cursa.module.modules.client;

import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.client.FontManager;
import net.spartanb312.cursa.client.GUIManager;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.engine.AsyncRenderer;
import net.spartanb312.cursa.engine.RenderEngine;
import net.spartanb312.cursa.event.events.render.RenderOverlayEvent;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.utils.Timer;
import net.spartanb312.cursa.utils.graphics.RenderHelper;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.Sys;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Parallel
@ModuleInfo(name = "ActiveModuleList", category = Category.CLIENT, description = "Show the active modules")
public class ActiveModuleList extends Module {

    Setting<ListPos> listPos = setting("ListPos", ListPos.RightTop).des("The position of list");

    AsyncRenderer asyncRenderer = new AsyncRenderer() {
        @Override
        public void onUpdate(ScaledResolution resolution, int mouseX, int mouseY) {
            int startX = RenderHelper.getStart(resolution, listPos.getValue()).x;
            int startY = RenderHelper.getStart(resolution, listPos.getValue()).y;

            if (mc.player.getActivePotionEffects().size() > 0 && listPos.getValue().equals(ListPos.RightTop)) {
                startY += 26;
            }

            int index = 0;

            List<Module> moduleList = Cursa.MODULE_BUS.getModules().stream()
                    .filter(Module::isVisible)
                    .sorted(Comparator.comparing(it -> -FontManager.getWidth(it.getHudSuffix()))).collect(Collectors.toList());

            for (Module module : moduleList) {
                int color = GUIManager.isRainbow() ? rainbow(index * -100) : GUIManager.getColor3I();
                index++;
                String information = module.getHudSuffix();
                switch (listPos.getValue()) {
                    case RightDown: {
                        drawAsyncString(information, startX - FontManager.getWidth(information), startY - FontManager.getHeight() * index, color);
                        break;
                    }
                    case LeftTop: {
                        drawAsyncString(information, startX, startY + 3 + FontManager.getHeight() * (index - 1), color);
                        break;
                    }
                    case LeftDown: {
                        drawAsyncString(information, startX, startY - FontManager.getHeight() * index, color);
                        break;
                    }
                    default: {
                        drawAsyncString(information, startX - FontManager.getWidth(information), startY + 3 + FontManager.getHeight() * (index - 1), color);
                        break;
                    }
                }
            }
        }
    };

    @Override
    public void onEnable() {
        RenderEngine.subscribe(asyncRenderer);
    }

    @Override
    public void onDisable() {
        RenderEngine.unsubscribe(asyncRenderer);
    }

    @Override
    public void onRender(RenderOverlayEvent event) {
        asyncRenderer.onRender();
    }

    public int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 1.0f, 1.0f).getRGB();
    }

    public enum ListPos {RightTop, RightDown, LeftTop, LeftDown}

}

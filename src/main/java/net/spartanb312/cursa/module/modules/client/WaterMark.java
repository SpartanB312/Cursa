package net.spartanb312.cursa.module.modules.client;

import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.client.FontManager;
import net.spartanb312.cursa.client.GUIManager;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.event.events.render.RenderOverlayEvent;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.utils.ChatUtil;

import java.awt.*;

@Parallel
@ModuleInfo(name = "WaterMark", category = Category.CLIENT, description = "Display the Cursa watermark")
public class WaterMark extends Module {

    private final Setting<Integer> x = setting("X", 0, 0, 3840);
    private final Setting<Integer> y = setting("Y", 0, 0, 2160);

    @Override
    public void onRender(RenderOverlayEvent event) {
        int color = GUIManager.isRainbow() ? rainbow(1) : GUIManager.getColor3I();
        FontManager.draw(Cursa.MOD_NAME + " " + ChatUtil.SECTIONSIGN + "f" + Cursa.MOD_VERSION, x.getValue() + 1, y.getValue() + 3, color);
    }

    public int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 1.0f, 1.0f).getRGB();
    }

}

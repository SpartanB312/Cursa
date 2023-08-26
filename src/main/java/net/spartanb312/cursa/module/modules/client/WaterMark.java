package net.spartanb312.cursa.module.modules.client;

import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.graphics.FontRenderers;
import net.spartanb312.cursa.client.GUIManager;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.event.events.render.RenderOverlayEvent;
import net.spartanb312.cursa.graphics.font.WordArt;
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
        if (GUIManager.isRainbow()) {
            WordArt.drawRainbowStringWithShadow(
                    FontRenderers.TitleFont,
                    Cursa.MOD_NAME,
                    x.getValue() + 5,
                    y.getValue() + 1,
                    2f,
                    1.25f,
                    0,
                    400f,
                    5000
            );
            int width = (int) FontRenderers.TitleFont.getWidth(Cursa.MOD_NAME, 2f);
            WordArt.drawRainbowStringWithShadow(
                    FontRenderers.TitleFont,
                    Cursa.MOD_VERSION,
                    x.getValue() + 9 + width,
                    y.getValue() + 5.25f,
                    .8f,
                    1.25f,
                    10,
                    400f,
                    5000
            );
            WordArt.drawRainbowStringWithShadow(
                    FontRenderers.TitleFont,
                    Cursa.MOD_BRANCH,
                    x.getValue() + 9 + width,
                    y.getValue() + 12.75f,
                    .8f,
                    1.25f,
                    10,
                    400f,
                    5000
            );
        } else {
            Color color = new Color(GUIManager.getColor3I());
            FontRenderers.TitleFont.drawStringWithShadow(
                    Cursa.MOD_NAME,
                    x.getValue() + 5,
                    y.getValue() + 1,
                    1.25f,
                    color,
                    2f
            );
            int width = (int) FontRenderers.TitleFont.getWidth(Cursa.MOD_NAME, 2f);
            FontRenderers.TitleFont.drawStringWithShadow(
                    Cursa.MOD_VERSION,
                    x.getValue() + 9 + width,
                    y.getValue() + 5.25f,
                    1.25f,
                    color,
                    .8f
            );
            FontRenderers.TitleFont.drawStringWithShadow(
                    Cursa.MOD_BRANCH,
                    x.getValue() + 9 + width,
                    y.getValue() + 12.75f,
                    1.25f,
                    color,
                    .8f
            );
        }
    }

}

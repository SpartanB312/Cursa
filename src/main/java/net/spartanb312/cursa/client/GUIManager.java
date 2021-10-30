package net.spartanb312.cursa.client;

import net.spartanb312.cursa.module.modules.client.GUISetting;
import net.spartanb312.cursa.utils.ColorUtil;

import java.awt.*;

public class GUIManager {

    public static GUISetting guiSetting;

    public static void init() {
        guiSetting = (GUISetting) ModuleManager.getModule(GUISetting.class);
    }

    public static boolean isParticle() {
        return guiSetting.particle.getValue();
    }

    public static boolean isRainbow() {
        return guiSetting.rainbow.getValue();
    }

    public static int getRed() {
        return ColorUtil.getRed(getColor3I());
    }

    public static int getGreen() {
        return ColorUtil.getGreen(getColor3I());
    }

    public static int getBlue() {
        return ColorUtil.getBlue(getColor3I());
    }

    public static int getAlpha() {
        return guiSetting.transparency.getValue();
    }

    public static int getColor3I() {
        if (guiSetting.rainbow.getValue()) {
            return getRainbowColor();
        } else {
            return new Color(guiSetting.red.getValue(), guiSetting.green.getValue(), guiSetting.blue.getValue()).getRGB();
        }
    }

    public static int getColor4I() {
        if (guiSetting.rainbow.getValue()) {
            int colorHex = getRainbowColor();
            return new Color(ColorUtil.getRed(colorHex), ColorUtil.getGreen(colorHex), ColorUtil.getBlue(colorHex), guiSetting.transparency.getValue()).getRGB();
        } else {
            return new Color(guiSetting.red.getValue(), guiSetting.green.getValue(), guiSetting.blue.getValue(), guiSetting.transparency.getValue()).getRGB();
        }
    }

    private static int getRainbowColor() {
        final float[] hue = {(System.currentTimeMillis() % (360 * 32)) / (360f * 32) * guiSetting.rainbowSpeed.getValue()};
        return Color.HSBtoRGB(hue[0], guiSetting.rainbowSaturation.getValue(), guiSetting.rainbowBrightness.getValue());
    }


    public static GUISetting.Background getBackground() {
        return guiSetting.background.getValue();
    }
}

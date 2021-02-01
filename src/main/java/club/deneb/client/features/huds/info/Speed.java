package club.deneb.client.features.huds.info;

import club.deneb.client.client.GuiManager;
import club.deneb.client.features.Category;
import club.deneb.client.features.HUDModule;
import club.deneb.client.utils.ChatUtil;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by B_312 on 01/03/21
 */
@HUDModule.Info(name = "Speed", x = 180, y = 180, width = 100, height = 10,category = Category.INFO)
public class Speed extends HUDModule {

    @Override
    public void onRender() {

        int fontColor = new Color(GuiManager.getINSTANCE().getRed() / 255f, GuiManager.getINSTANCE().getGreen() / 255f, GuiManager.getINSTANCE().getBlue() / 255f, 1F).getRGB();

        String Final = "km/h" + ChatUtil.SECTIONSIGN + "f " + speed();

        font.drawString(Final, this.x + 2, this.y + 4, fontColor);

        this.height = font.getHeight() * 2;
        this.width = font.getStringWidth(Final) + 4;

    }

    private static final DecimalFormat formatter = new DecimalFormat("#.#");

    public static String speed() {
        float currentTps = mc.timer.tickLength / 1000.0f;
        double multiply = 3.6; // convert mps to kmh
        return formatter.format(((MathHelper.sqrt(Math.pow(coordsDiff('x'), 2) + Math.pow(coordsDiff('z'), 2)) / currentTps)) * multiply);
    }

    private static double coordsDiff(char s) {
        switch (s) {
            case 'x': return mc.player.posX - mc.player.prevPosX;
            case 'z': return mc.player.posZ - mc.player.prevPosZ;
            default: return 0.0;
        }
    }

}

package club.deneb.client.features.huds.info;

import club.deneb.client.client.GuiManager;
import club.deneb.client.features.Category;
import club.deneb.client.features.HUDModule;
import club.deneb.client.utils.ChatUtil;
import club.deneb.client.utils.LagCompensator;

import java.awt.*;

/**
 * Created by B_312 on 01/03/21
 */
@HUDModule.Info(name = "TPS", x = 160, y = 160, width = 100, height = 10,category = Category.INFO)
public class TPS extends HUDModule {

    @Override
    public void onRender() {

        int fontColor = new Color(GuiManager.getINSTANCE().getRed() / 255f, GuiManager.getINSTANCE().getGreen() / 255f, GuiManager.getINSTANCE().getBlue() / 255f, 1F).getRGB();

        String Final = "TPS " + ChatUtil.SECTIONSIGN + "f" + String.format("%.2f", LagCompensator.INSTANCE.getTickRate());

        font.drawString(Final, this.x + 2, this.y + 4, fontColor);

        this.height = font.getHeight() * 2;
        this.width = font.getStringWidth(Final) + 4;

    }

}

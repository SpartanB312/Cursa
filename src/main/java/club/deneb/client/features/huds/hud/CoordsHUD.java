package club.deneb.client.features.huds.hud;

import club.deneb.client.client.GuiManager;
import club.deneb.client.features.HUDModule;
import club.deneb.client.utils.ChatUtil;

import java.awt.*;

/**
 * Created by B_312 on 01/03/21
 */
@HUDModule.Info(name = "CoordsHUD", x = 150, y = 150, width = 100, height = 10)
public class CoordsHUD extends HUDModule {

    @Override
    public void onRender() {

        int fontColor = new Color(GuiManager.getINSTANCE().getRed() / 255f, GuiManager.getINSTANCE().getGreen() / 255f, GuiManager.getINSTANCE().getBlue() / 255f, 1F).getRGB();

        boolean inHell = mc.player.dimension == -1;
        float f = !inHell ? 0.125f : 8.0f;

        String posX = String.format("%.1f", mc.player.posX);
        String posY = String.format("%.1f", mc.player.posY);
        String posZ = String.format("%.1f", mc.player.posZ);
        String hposX = String.format("%.1f", mc.player.posX * (double) f);
        String hposZ = String.format("%.1f", mc.player.posZ * (double) f);
        String ow = posX + ", " + posY + ", " + posZ;
        String nether = hposX + ", " + posY + ", " + hposZ;

        String Final = ChatUtil.SECTIONSIGN + "rXYZ " + ChatUtil.SECTIONSIGN + "f" + ow + ChatUtil.SECTIONSIGN + "r [" + ChatUtil.SECTIONSIGN + "f" + nether + ChatUtil.SECTIONSIGN + "r]";

        font.drawString(Final, this.x + 2, this.y + 4, fontColor);

        this.height = font.getHeight() * 2;
        this.width = font.getStringWidth(Final) + 4;

    }

}

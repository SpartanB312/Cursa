package com.deneb.client.features.huds.info;

import com.deneb.client.client.GuiManager;
import com.deneb.client.features.Category;
import com.deneb.client.features.HUDModule;
import com.deneb.client.utils.ChatUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;

/**
 * Created by B_312 on 01/03/21
 */
@HUDModule.Info(name = "Players", x = 160, y = 160, width = 100, height = 10,category = Category.INFO)
public class Players extends HUDModule {

    @Override
    public void onRender() {

        int fontColor = new Color(GuiManager.getINSTANCE().getRed() / 255f, GuiManager.getINSTANCE().getGreen() / 255f, GuiManager.getINSTANCE().getBlue() / 255f, 1F).getRGB();

        int OnlinePlayer = mc.player.connection.getPlayerInfoMap().size();

        String Final = "Player" + (OnlinePlayer > 1 ? "s" : "") + " " + ChatUtil.SECTIONSIGN + "f" + OnlinePlayer;

        font.drawString(Final, this.x + 2, this.y + 4, fontColor);

        this.height = font.getHeight() * 2;
        this.width = font.getStringWidth(Final) + 4;

    }

}

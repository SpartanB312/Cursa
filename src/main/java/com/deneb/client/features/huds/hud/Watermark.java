package com.deneb.client.features.huds.hud;

import com.deneb.client.Deneb;
import com.deneb.client.client.GuiManager;
import com.deneb.client.features.Category;
import com.deneb.client.features.HUDModule;
import com.deneb.client.utils.ChatUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;

/**
 * Created by B_312 on 01/03/21
 */
@HUDModule.Info(name = "Watermark", x = 100, y = 100, width = 100, height = 10)
public class Watermark extends HUDModule {

    @Override
    public void onRender() {

        int fontColor = new Color(GuiManager.getINSTANCE().getRed() / 255f, GuiManager.getINSTANCE().getGreen() / 255f, GuiManager.getINSTANCE().getBlue() / 255f, 1F).getRGB();

        font.drawString(Deneb.MODNAME + " "  + Deneb.VERSION , this.x + 2, this.y + 4, fontColor);

        this.height = font.getHeight() * 2;
        this.width = font.getStringWidth(Deneb.MODNAME + " "  + Deneb.VERSION ) + 4;

    }

}

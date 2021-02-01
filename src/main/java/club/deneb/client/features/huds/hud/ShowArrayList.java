package club.deneb.client.features.huds.hud;

import club.deneb.client.client.GuiManager;
import club.deneb.client.features.ModuleManager;
import club.deneb.client.features.HUDModule;
import club.deneb.client.features.IModule;
import club.deneb.client.features.Module;
import club.deneb.client.utils.ChatUtil;
import club.deneb.client.utils.clazz.ActivedModule;
import club.deneb.client.value.BooleanValue;
import club.deneb.client.value.ModeValue;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by B_312 on 01/03/21
 */
@HUDModule.Info(name = "ArrayList",x=50,y=50,width = 100,height = 100)
public class ShowArrayList extends HUDModule {

    BooleanValue sort = setting("SortList", true);
    ModeValue theme = setting("Theme",new ModeValue.Mode("Colorful",true),new ModeValue.Mode("Brief"),new ModeValue.Mode("Small"));
    ModeValue listPos = setting("ListPos", new ModeValue.Mode("RightTop", true), new ModeValue.Mode("RightDown"), new ModeValue.Mode("LeftTop"), new ModeValue.Mode("LeftDown"));

    private String getArrayList(IModule module) {
        return module.getName() + (module.getHudInfo() == null
                || module.getHudInfo().equals("") ? "" : " " + ChatUtil.SECTIONSIGN + "7" + (module.getHudInfo().equals("")
                || module.getHudInfo() == null ? "" : "[") + ChatUtil.SECTIONSIGN + "f" + module.getHudInfo() + '\u00a7' + "7" + (module.getHudInfo().equals("") ? "" : "]"));
    }

    @Override
    public void onRender(){

        int fontColor = new Color(GuiManager.getINSTANCE().getRed() / 255f, GuiManager.getINSTANCE().getGreen() / 255f, GuiManager.getINSTANCE().getBlue() / 255f, 1F).getRGB();

        List<ActivedModule> modList = new ArrayList<>();

        ModuleManager.getModules().stream().filter(IModule::isEnabled).forEach(module -> {
            if(((Module)module).isShownOnArray()) {
                String string = getArrayList(module);
                modList.add(new ActivedModule((Module) module, string));
            }
        });

        if (sort.getValue()) {
            if(theme.page("Colorful")) modList.sort((string, string2) -> font.getStringWidth(string2.string()) - font.getStringWidth(string.string()));
            else modList.sort((string, string2) -> mc.fontRenderer.getStringWidth(string2.string()) - mc.fontRenderer.getStringWidth(string.string()));
        }

        int y;

        if(listPos.getMode("RightTop").isToggled() ||listPos.getMode("LeftTop").isToggled() ) {
            y = this.y;
        } else {
            y = this.y - 14;
        }


        AtomicInteger offset = new AtomicInteger(y);
        AtomicInteger index = new AtomicInteger();

        int maxWidth = 0;

        int i = 0;
        for (ActivedModule string : modList) {

            int width = theme.page("Colorful") ? font.getStringWidth(string.string()) + 4 : mc.fontRenderer.getStringWidth(string.string());
            if(width > maxWidth) maxWidth = width;

            int rgb = rainbow(index.get() * 100);
            int red = rgb >> 16 & 255;
            int green = rgb >> 8 & 255;
            int blue = rgb & 255;
            int color;

            color = GuiManager.getINSTANCE().isRainbow() ? new Color(red,green,blue).getRGB() : fontColor;

            int x=this.x;

            int rect = new Color(10, 10, 10, 127).getRGB();

            if(listPos.getMode("RightTop").isToggled() ||listPos.getMode("LeftTop").isToggled() ) i++;else i--;

            if(listPos.getMode("RightTop").isToggled() ||listPos.getMode("RightDown").isToggled() ) {
                x = this.x - width;
                switch (theme.getToggledMode().getName()) {
                    case "Colorful" : {
                        Gui.drawRect(x, offset.get() + 1, x + width, offset.get() + 13, rect);
                        Gui.drawRect(x + width - 2, offset.get() + 1, x + width, offset.get() + 13, color);
                        font.drawString(string.string(), x + 1, offset.get() + 5.5f, color);
                        break;
                    }
                    case "Small" : {
                        if(listPos.page("RightTop")) {
                            mc.fontRenderer.drawString(string.string(), x, this.y + mc.fontRenderer.FONT_HEIGHT * i -9, color);
                        } else {
                            mc.fontRenderer.drawString(string.string(), x, this.y + mc.fontRenderer.FONT_HEIGHT * i, color);
                        }
                        break;
                    }
                    default:{
                        if(listPos.page("RightTop")) {
                            mc.fontRenderer.drawString(string.string(), x, this.y + mc.fontRenderer.FONT_HEIGHT * i -9, new Color(255, 255, 255).getRGB());
                        } else {
                            mc.fontRenderer.drawString(string.string(), x, this.y + mc.fontRenderer.FONT_HEIGHT * i, new Color(255, 255, 255).getRGB());
                        }
                        break;
                    }
                }
            } else {
                switch (theme.getToggledMode().getName()) {
                    case "Colorful": {
                        Gui.drawRect(x, offset.get() + 1, x + width, offset.get() + 13, rect);
                        Gui.drawRect(x, offset.get() + 1, x + 2, offset.get() + 13, color);
                        font.drawString(string.string(), x + 3, offset.get() + 5.5f, color);
                        break;
                    }
                    case "Small" : {
                        if(listPos.page("LeftTop")) {
                            mc.fontRenderer.drawString(string.string(), x, this.y + mc.fontRenderer.FONT_HEIGHT * i -9, color);
                        } else {
                            mc.fontRenderer.drawString(string.string(), x, this.y + mc.fontRenderer.FONT_HEIGHT * i, color);
                        }
                        break;
                    }
                    default:{
                        if(listPos.page("LeftTop")) {
                            mc.fontRenderer.drawString(string.string(), x, this.y + mc.fontRenderer.FONT_HEIGHT * i -9, new Color(255, 255, 255).getRGB());
                        } else {
                            mc.fontRenderer.drawString(string.string(), x, this.y + mc.fontRenderer.FONT_HEIGHT * i, new Color(255, 255, 255).getRGB());
                        }
                        break;
                    }
                }
            }

            offset.addAndGet(listPos.getMode("RightTop").isToggled() || listPos.getMode("LeftTop").isToggled()? 12 : -12);

            index.getAndIncrement();
        }

        this.width = maxWidth + 8;

        if (listPos.page("RightTop") || listPos.page("RightDown")) {
            this.width = -(maxWidth + 8);
        } else {
            this.width = maxWidth + 8;
        }

        if(listPos.getMode("RightTop").isToggled() ||listPos.getMode("LeftTop").isToggled() ) {
            this.height = theme.page("Colorful") ? modList.size() * 12 + 4 : mc.fontRenderer.FONT_HEIGHT * modList.size();
        } else {
            this.height = theme.page("Colorful") ? modList.size() * - 12 - 4 : - mc.fontRenderer.FONT_HEIGHT * modList.size();
        }

        modList.clear();
    }

    public static int rainbow(int delay) {
        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 1.0f, 1.0f).getRGB();
    }



}

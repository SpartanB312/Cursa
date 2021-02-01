package club.deneb.client.features.huds.hud;

import club.deneb.client.client.NotificationManager;
import club.deneb.client.features.HUDModule;
import club.deneb.client.utils.ChatUtil;
import club.deneb.client.value.IntValue;
import net.minecraft.client.gui.Gui;

import java.awt.*;

@HUDModule.Info(name = "Notification",x = 200 ,y = 200,width = 100,height = 100)
public class NotificationHUD extends HUDModule {

    IntValue deadTime = setting("VanishTimer",1000,0,5000);

    @Override
    public void onRender(){
        int maxWidth = 0;
        int startX = this.x;
        int startY = this.y;

        NotificationManager.getUnits().removeIf(v -> v.timer.passed(deadTime.getValue()));

        for(NotificationManager.NotificationUnit unit : NotificationManager.getUnits()){
            if (font.getStringWidth(getUnitString(unit)) + 4 > maxWidth) maxWidth = font.getStringWidth(getUnitString(unit)) + 4;
            Gui.drawRect(startX,startY,startX+font.getStringWidth(getUnitString(unit)) + 4,startY + font.getHeight() * 2,0x85000000);
            Gui.drawRect(startX,startY+font.getHeight() * 2 - 1, (int) (startX+getRate(unit)),startY + font.getHeight() * 2,new Color(16,192,255).getRGB());
            font.drawString(getUnitString(unit),startX + 1,startY + 5,new Color(255,255,255).getRGB());
            startY += font.getHeight() * 2;
        }

        this.width = maxWidth != 0 ? maxWidth : 100;
        int temp = (font.getHeight() * 2) * NotificationManager.getUnits().size();
        this.height = temp != 0 ? temp : 10;
    }

    public double getRate(NotificationManager.NotificationUnit unit){
        return (font.getStringWidth(getUnitString(unit)) + 4) * (deadTime.getValue() - unit.timer.hasPassed()) / deadTime.getValue();
    }

    public String getUnitString(NotificationManager.NotificationUnit unit){
        return unit.enabled ? unit.module.getName() + " " + ChatUtil.SECTIONSIGN + "a" + " Enabled":
                unit.module.getName() + " " + ChatUtil.SECTIONSIGN + "c" + " Disabled";
    }
}

package club.deneb.client.features.modules.misc;

import club.deneb.client.event.events.client.GuiScreenEvent;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.Value;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by 086 on 9/04/2018.
 */
@Module.Info(name = "AutoReconnect", description = "Automatically reconnects after being disconnected", category = Category.MISC)
public class AutoReconnect extends Module {

    Value<Integer> seconds = setting("Seconds",5,0,100);
    private static ServerData cServer;

    public static AutoReconnect INSTANCE;
    @Override
    public void onInit(){
        INSTANCE = this;
        MinecraftForge.EVENT_BUS.register(new alwaysListening());
    }

    public static class alwaysListening{
        @SubscribeEvent
        public void closedListener(GuiScreenEvent.Closed event){
            if (event.getScreen() instanceof GuiConnecting)
                cServer = mc.currentServerData;
        }

        @SubscribeEvent
        public void displayedListener(GuiScreenEvent.Displayed event){
            if (AutoReconnect.INSTANCE.isEnabled() && event.getScreen() instanceof GuiDisconnected && (cServer != null || mc.currentServerData != null))
                event.setScreen(new guiDisconnected((GuiDisconnected) event.getScreen()));
        }
    }

    public static class guiDisconnected extends GuiDisconnected {

        int millis = INSTANCE.seconds.getValue() * 1000;
        long cTime;

        public guiDisconnected(GuiDisconnected disconnected) {
            super(disconnected.parentScreen, disconnected.reason, disconnected.message);
            cTime = System.currentTimeMillis();
        }

        @Override
        public void updateScreen() {
            if (millis <= 0)
                mc.displayGuiScreen(new GuiConnecting(parentScreen, mc, cServer == null ? mc.currentServerData : cServer));
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            super.drawScreen(mouseX, mouseY, partialTicks);

            long a = System.currentTimeMillis();
            millis -= a - cTime;
            cTime = a;

            String s = "Reconnecting in " + Math.max(0, Math.floor((double) millis / 100) / 10) + "s";
            fontRenderer.drawString(s, width / 2 - fontRenderer.getStringWidth(s) / 2, height - 16, 0xffffff, true);
        }

    }

}

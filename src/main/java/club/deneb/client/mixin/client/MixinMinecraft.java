package club.deneb.client.mixin.client;

import club.deneb.client.client.ConfigManager;
import club.deneb.client.event.events.client.GuiScreenEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * GuiScreen Event from KAMI by 086 on 17/11/2017.
 */
@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow
    public GuiScreen currentScreen;

    @Inject(method = "displayGuiScreen", at = @At("HEAD"), cancellable = true)
    public void displayGuiScreen(GuiScreen guiScreenIn, CallbackInfo info) {
        if(Minecraft.getMinecraft().currentScreen != null) {
            GuiScreenEvent.Closed screenEvent = new GuiScreenEvent.Closed(Minecraft.getMinecraft().currentScreen);
            MinecraftForge.EVENT_BUS.post(screenEvent);
            GuiScreenEvent.Displayed screenEvent1 = new GuiScreenEvent.Displayed(guiScreenIn);
            MinecraftForge.EVENT_BUS.post(screenEvent1);
        }
    }

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReport(Minecraft minecraft, CrashReport crashReport) {
        save();
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    public void shutdown(CallbackInfo info) {
        save();
    }

    private void save() {
        System.out.println("Shutting down: saving Deneb configuration");
        ConfigManager.saveAll();
        System.out.println("Configuration saved.");
    }

}

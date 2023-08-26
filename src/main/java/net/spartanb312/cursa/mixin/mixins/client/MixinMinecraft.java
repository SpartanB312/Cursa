package net.spartanb312.cursa.mixin.mixins.client;

import net.minecraftforge.fml.common.FMLLog;
import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.client.ConfigManager;
import net.spartanb312.cursa.event.decentraliized.DecentralizedClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.crash.CrashReport;
import net.spartanb312.cursa.event.events.client.*;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setWindowIcon()V", shift = At.Shift.AFTER))
    public void debug(CallbackInfo ci) {
        System.out.println("-------PASS-------");
    }

    @Inject(method = "displayGuiScreen", at = @At("HEAD"))
    public void displayGuiScreen(GuiScreen guiScreenIn, CallbackInfo info) {
        if (Minecraft.getMinecraft().currentScreen != null) {
            GuiScreenEvent.Closed screenEvent = new GuiScreenEvent.Closed(Minecraft.getMinecraft().currentScreen);
            Cursa.EVENT_BUS.post(screenEvent);
            GuiScreenEvent.Displayed screenEvent1 = new GuiScreenEvent.Displayed(guiScreenIn);
            Cursa.EVENT_BUS.post(screenEvent1);
        }
    }

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    public void runGameLoop(CallbackInfo ci) {
        Cursa.EVENT_BUS.post(new GameLoopEvent());
    }

    @Inject(method = "runTickKeyboard", at = @At(value = "INVOKE_ASSIGN", target = "org/lwjgl/input/Keyboard.getEventKeyState()Z", remap = false))
    private void onKeyEvent(CallbackInfo ci) {
        if (Minecraft.getMinecraft().currentScreen != null)
            return;

        boolean down = Keyboard.getEventKeyState();
        int key = Keyboard.getEventKey();
        char ch = Keyboard.getEventCharacter();

        //Prevent from toggling all modules,when switching languages.
        if (key != Keyboard.KEY_NONE)
            Cursa.EVENT_BUS.post(down ? new KeyEvent(key, ch) : new InputUpdateEvent(key, ch));
    }

    @Inject(method = "runTick", at = @At("RETURN"))
    public void onTick(CallbackInfo ci) {
        if (Minecraft.getMinecraft().player != null) {
            DecentralizedClientTickEvent.instance.post(null);
            Cursa.EVENT_BUS.post(new TickEvent());
        }
    }

    @Inject(method = "init", at = @At("HEAD"))
    public void onInitMinecraft(CallbackInfo ci) {
        Cursa.EVENT_BUS.register(Cursa.instance);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 0, shift = At.Shift.BEFORE))
    public void onPreInit(CallbackInfo callbackInfo) {
        Cursa.EVENT_BUS.post(new InitializationEvent.PreInitialize());
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;checkGLError(Ljava/lang/String;)V", ordinal = 2, shift = At.Shift.AFTER))
    public void onInit(CallbackInfo ci) {
        FMLLog.log.fatal("Loading Cursa");
        Cursa.EVENT_BUS.post(new InitializationEvent.Initialize());
    }

    @Inject(method = "init", at = @At("RETURN"))
    public void onPostInit(CallbackInfo ci) {
        Cursa.EVENT_BUS.post(new InitializationEvent.PostInitialize());
    }

    @Redirect(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayCrashReport(Lnet/minecraft/crash/CrashReport;)V"))
    public void displayCrashReport(Minecraft minecraft, CrashReport crashReport) {
        cursa$save();
    }

    @Inject(method = "shutdown", at = @At("HEAD"))
    public void shutdown(CallbackInfo info) {
        cursa$save();
    }

    @Unique
    private void cursa$save() {
        System.out.println("Shutting down: saving " + Cursa.MOD_NAME + " configuration");
        ConfigManager.saveAll();
        System.out.println("Configuration saved.");
    }

}

package net.spartanb312.cursa.mixin.mixins.render;

import net.minecraft.client.renderer.EntityRenderer;
import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.event.decentraliized.DecentralizedRenderTickEvent;
import net.spartanb312.cursa.event.decentraliized.DecentralizedRenderWorldEvent;
import net.spartanb312.cursa.event.events.render.HudOverlayEvent;
import net.spartanb312.cursa.event.events.render.RenderOverlayEvent;
import net.spartanb312.cursa.event.events.render.RenderWorldEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V", shift = At.Shift.AFTER))
    public void onRender2D(float p_181560_1_, long p_181560_2_, CallbackInfo ci) {
        RenderOverlayEvent event = new RenderOverlayEvent();
        DecentralizedRenderTickEvent.instance.post(event);
        Cursa.EVENT_BUS.post(event);
    }

    @Inject(method = "hurtCameraEffect", at = @At("HEAD"), cancellable = true)
    public void hurtCameraEffect(float partialTicks, CallbackInfo ci) {
        HudOverlayEvent event = new HudOverlayEvent(HudOverlayEvent.Type.HURTCAM);
        Cursa.EVENT_BUS.post(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Inject(method = "renderWorldPass", at = @At(value = "INVOKE_STRING", target = "net/minecraft/profiler/Profiler.endStartSection(Ljava/lang/String;)V", args = "ldc=hand"))
    public void onStartHand(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        RenderWorldEvent event = new RenderWorldEvent(partialTicks, pass);
        DecentralizedRenderWorldEvent.instance.post(event);
        Cursa.EVENT_BUS.post(event);
    }

}

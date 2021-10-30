package net.spartanb312.cursa.mixin.mixins.render;

import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.event.decentraliized.DecentralizedRenderTickEvent;
import net.spartanb312.cursa.event.events.render.RenderOverlayEvent;
import net.minecraft.client.gui.GuiSubtitleOverlay;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiSubtitleOverlay.class)
public class MixinGuiSubtitleOverlay {

    @Inject(method = "renderSubtitles", at = @At("HEAD"))
    public void onRender2D(ScaledResolution resolution, CallbackInfo ci) {
        RenderOverlayEvent event = new RenderOverlayEvent();
        DecentralizedRenderTickEvent.instance.post(event);
        Cursa.EVENT_BUS.post(event);
    }

}

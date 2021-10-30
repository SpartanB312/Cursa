package net.spartanb312.cursa.mixin.mixins.entity;

import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.event.events.client.ChatEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP {

    @Inject(method = "sendChatMessage", at = @At(value = "HEAD"), cancellable = true)
    public void sendChatPacket(String message, CallbackInfo ci) {
        ChatEvent event = new ChatEvent(message);
        Cursa.EVENT_BUS.post(event);
        if (event.isCancelled()) ci.cancel();
    }

}

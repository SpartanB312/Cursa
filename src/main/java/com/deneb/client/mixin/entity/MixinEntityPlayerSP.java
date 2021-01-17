package com.deneb.client.mixin.entity;

import com.deneb.client.event.events.entity.PlayerMoveEvent;
import com.deneb.client.event.events.entity.PlayerUpdateEvent;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayerSP.class, priority = 312312)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    public MixinEntityPlayerSP(World p_i45074_1_, GameProfile p_i45074_2_) {
        super(p_i45074_1_, p_i45074_2_);
    }

    @Inject(method = "onUpdate", at = @At("HEAD"), cancellable = true)
    public void onUpdate(CallbackInfo callbackInfo) {
        PlayerUpdateEvent event = new PlayerUpdateEvent();
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCancelled()) callbackInfo.cancel();
    }

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    public void move(MoverType type, double x, double y, double z, CallbackInfo info) {
        PlayerMoveEvent event = new PlayerMoveEvent(type, x, y, z);
        MinecraftForge.EVENT_BUS.post(event);
        if (event.isCancelled()) info.cancel();
    }

}
package com.deneb.client.features.modules.player;

import com.deneb.client.event.events.client.PacketEvent;
import com.deneb.client.event.events.entity.PlayerMoveEvent;
import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.value.IValue;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by 086 on 22/12/2017.
 */
@Module.Info(name = "Freecam", category = Category.PLAYER, description = "Leave your body and trascend into the realm of the gods")
public class Freecam extends Module {

   IValue speed = setting("Speed", 5,0,100);

    private double posX, posY, posZ;
    private float pitch, yaw;

    private EntityOtherPlayerMP clonedPlayer;

    private boolean isRidingEntity;
    private Entity ridingEntity;

    @Override
    public void onEnable() {
        if (mc.player != null) {
            isRidingEntity = mc.player.getRidingEntity() != null;

            if (mc.player.getRidingEntity() == null) {
                posX = mc.player.posX;
                posY = mc.player.posY;
                posZ = mc.player.posZ;
            } else {
                ridingEntity = mc.player.getRidingEntity();
                mc.player.dismountRidingEntity();
            }

            pitch = mc.player.rotationPitch;
            yaw = mc.player.rotationYaw;

            clonedPlayer = new EntityOtherPlayerMP(mc.world, mc.getSession().getProfile());
            clonedPlayer.copyLocationAndAnglesFrom(mc.player);
            clonedPlayer.rotationYawHead = mc.player.rotationYawHead;
            mc.world.addEntityToWorld(-100, clonedPlayer);
            mc.player.capabilities.isFlying = true;
            mc.player.capabilities.setFlySpeed(speed.getValue() / 100f);
            mc.player.noClip = true;
        }
    }

    @Override
    public void onDisable() {
        EntityPlayer localPlayer = mc.player;
        if (localPlayer != null) {
            mc.player.setPositionAndRotation(posX, posY, posZ, yaw, pitch);
            mc.world.removeEntityFromWorld(-100);
            clonedPlayer = null;
            posX = posY = posZ = 0.D;
            pitch = yaw = 0.f;
            mc.player.capabilities.isFlying = false; //getModManager().getMod("ElytraFlight").isEnabled();
            mc.player.capabilities.setFlySpeed(0.05f);
            mc.player.noClip = false;
            mc.player.motionX = mc.player.motionY = mc.player.motionZ = 0.f;

            if (isRidingEntity) {
                mc.player.startRiding(ridingEntity, true);
            }
        }
    }

    @Override
    public void onTick() {
        mc.player.capabilities.isFlying = true;
        mc.player.capabilities.setFlySpeed(speed.getValue() / 100f);
        mc.player.noClip = true;
        mc.player.onGround = false;
        mc.player.fallDistance = 0;
    }

    @SubscribeEvent
    public void moveListener(PlayerMoveEvent event){
        mc.player.noClip = true;
    }

    @SubscribeEvent
    public void pushListener (PlayerSPPushOutOfBlocksEvent event){
        event.setCanceled(true);
    }

    @SubscribeEvent
    public void sendListener (PacketEvent.Send event){
        if (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput) {
            event.cancel();
        }
    }

}

package com.deneb.client.features.modules.combat;

import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.features.ModuleManager;
import com.deneb.client.value.IValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.server.SPacketDisconnect;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

/**
 * Created by 086 on 9/04/2018.
 */
@Module.Info(name = "AutoLog", description = "Automatically log when in danger or on low health", category = Category.COMBAT)
public class AutoLog extends Module {

    IValue health = setting("Health",6,0,36);

    private boolean shouldLog = false;
    long lastLog = System.currentTimeMillis();

    @SubscribeEvent
    public void livingDamageEventListener (LivingDamageEvent event){
        if (mc.player == null) return;
        if (event.getEntity() == mc.player) {
            if (mc.player.getHealth() - event.getAmount() < health.getValue()) {
                log();
            }
        }
    }

    @SubscribeEvent
    public void entityJoinWorldEventListener(EntityJoinWorldEvent event){
        if (mc.player == null) return;
        if (event.getEntity() instanceof EntityEnderCrystal) {
            if (mc.player.getHealth() - AutoCrystal.calculateDamage(event.getEntity().posX,event.getEntity().posY,event.getEntity().posZ, mc.player) < health.getValue()) {
                log();
            }
        }
    }

    @Override
    public void onTick() {
        if (shouldLog) {
            shouldLog = false;
            if (System.currentTimeMillis() - lastLog < 2000) return;
            Objects.requireNonNull(Minecraft.getMinecraft().getConnection()).handleDisconnect(new SPacketDisconnect(new TextComponentString("AutoLogged")));
        }
    }

    private void log() {
        ModuleManager.getModuleByName("AutoReconnect").disable();
        shouldLog = true;
        lastLog = System.currentTimeMillis();
    }

}

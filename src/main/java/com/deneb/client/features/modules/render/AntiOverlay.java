package com.deneb.client.features.modules.render;

import com.deneb.client.event.events.client.PacketEvent;
import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.value.BValue;
import net.minecraft.init.MobEffects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnPainting;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(name = "AntiOverlay",category = Category.RENDER)
public class AntiOverlay extends Module {

    BValue fire = setting("Fire", true);
    BValue blocks = setting("Blocks", true);
    BValue water = setting("Water", true);
    BValue blindness = setting("Blindness", true);
    BValue nausea = setting("Nausea", false);
    BValue xp = setting("XP", false);
    BValue mob = setting("Mob", false);
    BValue explosion = setting("Explosions", true);
    BValue paint = setting("Paintings", false);

    @Override
    public void onTick() {
        if (blindness.getValue()) {
            mc.player.removeActivePotionEffect(MobEffects.BLINDNESS);
        }

        if (nausea.getValue()) {
            mc.player.removeActivePotionEffect(MobEffects.NAUSEA);
        }
    }

    @SubscribeEvent
    public void onBlockOverlayEvent(RenderBlockOverlayEvent event){
        if (this.isDisabled()) {
            return;
        }
        if (fire.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.FIRE)
            event.setCanceled(true);
        if (blocks.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.BLOCK)
            event.setCanceled(true);
        if (water.getValue() && event.getOverlayType() == RenderBlockOverlayEvent.OverlayType.WATER)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onPackReceive(PacketEvent.Receive event){
        if (this.isDisabled()) {
            return;
        }
        Packet packet = event.packet;
        if ((packet instanceof SPacketSpawnExperienceOrb && xp.getValue())
                || (packet instanceof SPacketExplosion && explosion.getValue())
                || (packet instanceof SPacketSpawnPainting && paint.getValue())
                || packet instanceof SPacketSpawnMob && mob.getValue()) {
            event.setCanceled(true);
        }
    }
}
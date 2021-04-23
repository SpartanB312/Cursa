package club.deneb.client.features.modules.render;

import club.deneb.client.event.events.client.PacketEvent;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.Value;
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

    Value<Boolean> fire = setting("Fire", true);
    Value<Boolean> blocks = setting("Blocks", true);
    Value<Boolean> water = setting("Water", true);
    Value<Boolean> blindness = setting("Blindness", true);
    Value<Boolean> nausea = setting("Nausea", false);
    Value<Boolean> xp = setting("XP", false);
    Value<Boolean> mob = setting("Mob", false);
    Value<Boolean> explosion = setting("Explosions", true);
    Value<Boolean> paint = setting("Paintings", false);

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
package net.spartanb312.cursa.module.modules.render;

import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.event.events.network.PacketEvent;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;
import net.minecraft.init.MobEffects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketSpawnExperienceOrb;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnPainting;

@Parallel(runnable = true)
@ModuleInfo(name = "AntiOverlay", category = Category.RENDER, description = "Cancel some overlay render")
public class AntiOverlay extends Module {

    Setting<Boolean> blindness = setting("Blindness", true);
    Setting<Boolean> nausea = setting("Nausea", false);
    Setting<Boolean> xp = setting("XP", false);
    Setting<Boolean> mob = setting("Mob", false);
    Setting<Boolean> explosion = setting("Explosions", true);
    Setting<Boolean> paint = setting("Paintings", false);

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (blindness.getValue()) {
            mc.player.removeActivePotionEffect(MobEffects.BLINDNESS);
        }

        if (nausea.getValue()) {
            mc.player.removeActivePotionEffect(MobEffects.NAUSEA);
        }
    }

    @Override
    public void onPacketReceive(PacketEvent.Receive event) {
        Packet<?> packet = event.packet;
        if ((packet instanceof SPacketSpawnExperienceOrb && xp.getValue())
                || (packet instanceof SPacketExplosion && explosion.getValue())
                || (packet instanceof SPacketSpawnPainting && paint.getValue())
                || packet instanceof SPacketSpawnMob && mob.getValue()) {
            event.cancel();
        }
    }

}
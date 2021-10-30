package net.spartanb312.cursa.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.event.events.network.PacketEvent;
import net.spartanb312.cursa.mixin.mixins.accessor.AccessorSPacketEntityVelocity;
import net.spartanb312.cursa.mixin.mixins.accessor.AccessorSPacketExplosion;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;

import javax.annotation.Nullable;

@Parallel
@ModuleInfo(name = "Velocity", category = Category.MOVEMENT, description = "Adjust entity velocity")
public class Velocity extends Module {

    Setting<Integer> horizontal_vel = setting("Horizontal", 0, 0, 100);
    Setting<Integer> vertical_vel = setting("Vertical", 0, 0, 100);
    Setting<Boolean> explosions = setting("Explosions", true);
    Setting<Boolean> bobbers = setting("Bobbers", true);

    public final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void onPacketReceive(PacketEvent.Receive event) {
        if (mc.player == null) return;
        if (event.packet instanceof SPacketEntityStatus && this.bobbers.getValue()) {
            final SPacketEntityStatus packet = (SPacketEntityStatus) event.packet;
            if (packet.getOpCode() == 31) {
                @Nullable final Entity entity = packet.getEntity(mc.world);
                if (entity != null) {
                    if (entity instanceof EntityFishHook) {
                        final EntityFishHook fishHook = (EntityFishHook) entity;
                        if (fishHook.caughtEntity == mc.player) {
                            event.cancel();
                        }
                    }
                }
            }
        }
        if (event.packet instanceof SPacketEntityVelocity) {
            final SPacketEntityVelocity packet = (SPacketEntityVelocity) event.packet;
            if (packet.getEntityID() == mc.player.getEntityId()) {
                if (this.horizontal_vel.getValue() == 0 && this.vertical_vel.getValue() == 0) {
                    event.cancel();
                    return;
                }

                if (this.horizontal_vel.getValue() != 100) {
                    ((AccessorSPacketEntityVelocity) packet).setMotionX(packet.getMotionX() / 100 * this.horizontal_vel.getValue());
                    ((AccessorSPacketEntityVelocity) packet).setMotionZ(packet.getMotionZ() / 100 * this.horizontal_vel.getValue());
                }

                if (this.vertical_vel.getValue() != 100) {
                    ((AccessorSPacketEntityVelocity) packet).setMotionY(packet.getMotionY() / 100 * this.vertical_vel.getValue());
                }
            }
        }
        if (event.packet instanceof SPacketExplosion && this.explosions.getValue()) {
            final SPacketExplosion packet = (SPacketExplosion) event.packet;

            if (this.horizontal_vel.getValue() == 0 && this.vertical_vel.getValue() == 0) {
                event.cancel();
                return;
            }

            if (this.horizontal_vel.getValue() != 100) {
                ((AccessorSPacketExplosion) packet).setMotionX(packet.getMotionX() / 100 * this.horizontal_vel.getValue());
                ((AccessorSPacketExplosion) packet).setMotionZ(packet.getMotionZ() / 100 * this.horizontal_vel.getValue());
            }

            if (this.vertical_vel.getValue() != 100) {
                ((AccessorSPacketExplosion) packet).setMotionY(packet.getMotionY() / 100 * this.vertical_vel.getValue());
            }
        }
    }
}

package club.deneb.client.features.modules.movement;

import club.deneb.client.event.events.client.PacketEvent;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.BooleanValue;
import club.deneb.client.value.IntValue;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Module from seppuku made by Seth
 */
@Module.Info(name = "Velocity",category = Category.MOVEMENT)
public class Velocity extends Module {

    IntValue horizontal_vel = setting("Horizontal",0,0,100);
    IntValue vertical_vel = setting("Vertical",0,0,100);
    BooleanValue explosions = setting("Explosions",true);
    BooleanValue bobbers = setting("Bobbers",true);

    public final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void receivePacket(PacketEvent.Receive event) {
        if (event.packet instanceof SPacketEntityStatus && this.bobbers.getValue()) {
            final SPacketEntityStatus packet = (SPacketEntityStatus) event.packet;
            if (packet.getOpCode() == 31) {
                final Entity entity = packet.getEntity(mc.world);
                if(entity != null) {
                    if (entity instanceof EntityFishHook) {
                        final EntityFishHook fishHook = (EntityFishHook) entity;
                        if (fishHook.caughtEntity == mc.player) {
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
        if (event.packet instanceof SPacketEntityVelocity) {
            final SPacketEntityVelocity packet = (SPacketEntityVelocity) event.packet;
            if (packet.getEntityID() == mc.player.getEntityId()) {
                if (this.horizontal_vel.getValue() == 0 && this.vertical_vel.getValue() == 0) {
                    event.setCanceled(true);
                    return;
                }

                if (this.horizontal_vel.getValue() != 100) {
                    packet.motionX = packet.motionX / 100 * this.horizontal_vel.getValue();
                    packet.motionZ = packet.motionZ / 100 * this.horizontal_vel.getValue();
                }

                if (this.vertical_vel.getValue() != 100) {
                    packet.motionY = packet.motionY / 100 * this.vertical_vel.getValue();
                }
            }
        }
        if (event.packet instanceof SPacketExplosion && this.explosions.getValue()) {
            final SPacketExplosion packet = (SPacketExplosion) event.packet;

            if (this.horizontal_vel.getValue() == 0 && this.vertical_vel.getValue() == 0) {
                event.setCanceled(true);
                return;
            }

            if (this.horizontal_vel.getValue() != 100) {
                packet.motionX = packet.motionX / 100 * this.horizontal_vel.getValue();
                packet.motionZ = packet.motionZ / 100 * this.horizontal_vel.getValue();
            }

            if (this.vertical_vel.getValue() != 100) {
                packet.motionY = packet.motionY / 100 * this.vertical_vel.getValue();
            }
        }
    }
}

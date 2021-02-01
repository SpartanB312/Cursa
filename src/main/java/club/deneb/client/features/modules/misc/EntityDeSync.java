package club.deneb.client.features.modules.misc;

import club.deneb.client.event.events.entity.PlayerUpdateEvent;
import club.deneb.client.event.events.client.PacketEvent;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.utils.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketSetPassengers;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Author ionar
 * Module from salhack
 */
@Module.Info(name = "EntityDeSync",category = Category.MISC,visible = false)
public class EntityDeSync extends Module {

    static EntityDeSync INSTANCE;

    @Override
    public void onInit() {
        INSTANCE = this;
    }

    private Entity Riding = null;

    @Override
    public void onEnable() {
        if (mc.player == null) {
            Riding = null;
            toggle();
            return;
        }
        if (!mc.player.isRiding()) {
            ChatUtil.printChatMessage("You are not riding an entity.");
            Riding = null;
            toggle();
            return;
        }
        ChatUtil.printChatMessage("Vanished");
        Riding = mc.player.getRidingEntity();
        mc.player.dismountRidingEntity();
        mc.world.removeEntity(Riding);
    }

    @Override
    public void onDisable() {
        if (Riding != null) {
            Riding.isDead = false;
            if (!mc.player.isRiding()) {
                mc.world.spawnEntity(Riding);
                mc.player.startRiding(Riding, true);
            }
            Riding = null;
            ChatUtil.printChatMessage("Remounted.");
        }
    }

    @SubscribeEvent
    public void OnPlayerUpdate (PlayerUpdateEvent event){
        if (Riding == null)
            return;

        if (mc.player.isRiding())
            return;

        mc.player.onGround = true;

        Riding.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ);

        mc.player.connection.sendPacket(new CPacketVehicleMove(Riding));
    }

    @SubscribeEvent
    public void onPacket (PacketEvent.Receive event) {
        if (event.packet instanceof SPacketSetPassengers) {
            if (Riding == null)
                return;
            SPacketSetPassengers l_Packet = (SPacketSetPassengers) event.packet;
            Entity en = mc.world.getEntityByID(l_Packet.getEntityId());
            if (en == Riding) {
                for (int i : l_Packet.getPassengerIds()) {
                    Entity ent = mc.world.getEntityByID(i);
                    if (ent == mc.player)
                        return;
                }
                ChatUtil.printChatMessage("You dismounted");
                toggle();
            }
        }
        else if (event.packet instanceof SPacketDestroyEntities) {
            SPacketDestroyEntities l_Packet = (SPacketDestroyEntities) event.packet;
            for (int l_EntityId : l_Packet.getEntityIDs()) {
                if (l_EntityId == Riding.getEntityId()) {
                    ChatUtil.printChatMessage("Entity is now null!");
                    return;
                }
            }
        }
    }

    @SubscribeEvent
    public void OnWorldEvent(EntityJoinWorldEvent event) {
        if (event.getEntity() == mc.player) {
            ChatUtil.printChatMessage("Player " + event.getEntity().getName() +  " joined the world!");
        }
    }
}

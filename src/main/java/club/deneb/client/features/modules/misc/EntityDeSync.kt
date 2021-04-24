package club.deneb.client.features.modules.misc

import club.deneb.client.event.events.client.PacketEvent
import club.deneb.client.event.events.entity.PlayerUpdateEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.utils.ChatUtil
import net.minecraft.entity.Entity
import net.minecraft.network.play.client.CPacketVehicleMove
import net.minecraft.network.play.server.SPacketDestroyEntities
import net.minecraft.network.play.server.SPacketSetPassengers
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Module.Info(
    name = "EntityDeSync",
    description = "Use this to vanish the entity which you are riding on",
    category = Category.MISC,
    visible = false
)
class EntityDeSync : Module() {

    private var riding: Entity? = null
    override fun onEnable() {
        if (mc.player == null) {
            riding = null
            toggle()
            return
        }
        if (!mc.player.isRiding) {
            ChatUtil.printChatMessage("You are not riding an entity.")
            riding = null
            toggle()
            return
        }
        ChatUtil.printChatMessage("Vanished")
        riding = mc.player.getRidingEntity()
        mc.player.dismountRidingEntity()
        mc.world.removeEntity(riding!!)
    }

    override fun onDisable() {
        if (riding != null) {
            riding!!.isDead = false
            if (!mc.player.isRiding) {
                mc.world.spawnEntity(riding!!)
                mc.player.startRiding(riding!!, true)
            }
            riding = null
            ChatUtil.printChatMessage("Remounted.")
        }
    }

    @SubscribeEvent
    fun onPlayerUpdate(event: PlayerUpdateEvent?) {
        if (riding == null) return
        if (mc.player.isRiding) return
        mc.player.onGround = true
        riding!!.setPosition(mc.player.posX, mc.player.posY, mc.player.posZ)
        mc.player.connection.sendPacket(CPacketVehicleMove(riding!!))
    }

    @SubscribeEvent
    fun onPacket(event: PacketEvent.Receive) {
        if (event.packet is SPacketSetPassengers) {
            if (riding == null) return
            val packet = event.packet as SPacketSetPassengers
            val en = mc.world.getEntityByID(packet.entityId)
            if (en === riding) {
                for (i in packet.passengerIds) {
                    val ent = mc.world.getEntityByID(i)
                    if (ent === mc.player) return
                }
                ChatUtil.printChatMessage("You dismounted")
                toggle()
            }
        } else if (event.packet is SPacketDestroyEntities) {
            val packet = event.packet as SPacketDestroyEntities
            for (l_EntityId in packet.entityIDs) {
                if (l_EntityId == riding!!.getEntityId()) {
                    ChatUtil.printChatMessage("Entity is now null!")
                    return
                }
            }
        }
    }

    @SubscribeEvent
    fun onWorldEvent(event: EntityJoinWorldEvent) {
        if (event.entity === mc.player) {
            ChatUtil.printChatMessage("Player " + event.entity.name + " joined the world!")
        }
    }

}
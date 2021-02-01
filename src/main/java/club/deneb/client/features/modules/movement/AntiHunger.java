package club.deneb.client.features.modules.movement;

import club.deneb.client.event.events.client.PacketEvent;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by 086 on 8/04/2018.
 */
@Module.Info(name = "AntiHunger", category = Category.MOVEMENT, description = "Lose hunger less fast. Might cause ghostblocks.")
public class AntiHunger extends Module {

    @SubscribeEvent
    public void packetListener(PacketEvent.Send event){
        if (event.getPacket() instanceof CPacketPlayer) {
            ((CPacketPlayer) event.getPacket()).onGround = false;
        }
    }

}

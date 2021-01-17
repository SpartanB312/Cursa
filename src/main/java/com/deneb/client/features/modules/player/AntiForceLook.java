package com.deneb.client.features.modules.player;

import com.deneb.client.event.events.client.PacketEvent;
import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by 086 on 12/12/2017.
 */
@Module.Info(name = "AntiForceLook", category = Category.PLAYER)
public class AntiForceLook extends Module {

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event){
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
        }
    }

}

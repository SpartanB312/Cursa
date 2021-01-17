package com.deneb.client.features.modules.player;

import com.deneb.client.event.events.client.PacketEvent;
import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**
 * Created by GlowskiBroski on 10/14/2018.
 */
@Module.Info(name = "PortalGodMode", category = Category.PLAYER)
public class PortalGodMode extends Module {
    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (isEnabled() && event.getPacket() instanceof CPacketConfirmTeleport) {
            event.cancel();
        }
    }
}

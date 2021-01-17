package com.deneb.client.features.modules.misc;

import com.deneb.client.Deneb;
import com.deneb.client.event.events.client.PacketEvent;
import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.value.BValue;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by 086 on 8/04/2018.
 */
@Module.Info(name = "CustomChat", category = Category.MISC, description = "Modifies your chat messages")
public class CustomChat extends Module {

    BValue commands = setting("Commands", false);

    @SubscribeEvent
    public void listener (PacketEvent.Send event){
        if (event.getPacket() instanceof CPacketChatMessage) {
            String s = ((CPacketChatMessage) event.getPacket()).getMessage();
            if (s.startsWith("/") && !commands.getValue()) return;
            s += Deneb.CHAT_SUFFIX;
            if (s.length() >= 256) s = s.substring(0,256);
            ((CPacketChatMessage) event.getPacket()).message = s;
        }
    }

}

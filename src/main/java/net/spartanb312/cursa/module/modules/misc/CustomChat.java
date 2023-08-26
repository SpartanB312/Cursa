package net.spartanb312.cursa.module.modules.misc;

import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.event.events.network.PacketEvent;
import net.spartanb312.cursa.mixin.mixins.accessor.AccessorCPacketChatMessage;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;
import net.minecraft.network.play.client.CPacketChatMessage;

@Parallel
@ModuleInfo(name = "CustomChat", category = Category.MISC, description = "Append a suffix on chat message")
public class CustomChat extends Module {

    Setting<Boolean> commands = setting("Commands", false);

    @Override
    public void onPacketSend(PacketEvent.Send event) {
        if (event.packet instanceof CPacketChatMessage) {
            String s = ((CPacketChatMessage) event.getPacket()).getMessage();
            if (s.startsWith("/") && !commands.getValue()) return;
            s += (" " + Cursa.CHAT_SUFFIX);
            if (s.length() >= 256) s = s.substring(0, 256);
            ((AccessorCPacketChatMessage) event.getPacket()).setMessage(s);
        }
    }

}

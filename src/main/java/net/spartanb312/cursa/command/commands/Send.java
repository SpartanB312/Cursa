package net.spartanb312.cursa.command.commands;

import net.spartanb312.cursa.command.Command;
import net.spartanb312.cursa.common.annotations.CommandInfo;
import net.spartanb312.cursa.utils.ChatUtil;
import net.minecraft.network.play.client.CPacketChatMessage;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@CommandInfo(command = "say", description = "Send message to chat.")
public class Send extends Command {

    @Override
    public void onCall(String s, String[] args) {
        try {
            StringBuilder content = new StringBuilder();
            for (String arg : args) {
                content.append(" ").append(arg);
            }
            mc.player.connection.sendPacket(new CPacketChatMessage(content.toString()));
        } catch (Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "say <message>";
    }

}

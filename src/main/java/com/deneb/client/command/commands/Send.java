package com.deneb.client.command.commands;

import com.deneb.client.command.Command;
import com.deneb.client.utils.ChatUtil;
import com.deneb.client.utils.Wrapper;
import net.minecraft.network.play.client.CPacketChatMessage;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "say", description = "Send message to chat.")
public class Send extends Command {

    @Override
    public void onCall(String s, String[] args) {
        try {
            String content = "";
            for (String arg : args) {
                content = content + " " + arg;
            }
            Wrapper.getPlayer().connection.sendPacket(new CPacketChatMessage(content));
        } catch (Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "say <message>";
    }

}

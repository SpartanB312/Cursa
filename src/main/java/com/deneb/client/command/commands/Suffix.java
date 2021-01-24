package com.deneb.client.command.commands;

import com.deneb.client.Deneb;
import com.deneb.client.command.Command;
import com.deneb.client.features.modules.misc.FakePlayer;
import com.deneb.client.utils.ChatUtil;

import java.util.Arrays;

/**
 * Created by B_312 on 01/25/21
 */
@Command.Info(command = "fpn", description = "Customize chat suffix.")
public class Suffix extends Command {

    @Override
    public void onCall(String s, String[] args) {
        if (args[0] == null) {
            ChatUtil.sendNoSpamMessage(getSyntax());
            return;
        }
        StringBuilder builder = new StringBuilder();
        Arrays.stream(args).forEach(arg -> builder.append(arg).append(" "));
        Deneb.CHAT_SUFFIX = builder.toString();
        ChatUtil.sendNoSpamMessage("Set chat suffix to "+ builder.toString());
    }

    @Override
    public String getSyntax() {
        return "suffix <message>";
    }

}

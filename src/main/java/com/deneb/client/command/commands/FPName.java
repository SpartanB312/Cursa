package com.deneb.client.command.commands;

import com.deneb.client.client.ConfigManager;
import com.deneb.client.command.Command;
import com.deneb.client.features.modules.misc.FakePlayer;
import com.deneb.client.utils.ChatUtil;

/**
 * Created by B_312 on 01/25/21
 */
@Command.Info(command = "fpn", description = "Customize fake player name.")
public class FPName extends Command {

    @Override
    public void onCall(String s, String[] args) {
        if (args[0] == null) {
            ChatUtil.sendNoSpamMessage(getSyntax());
            return;
        }
        FakePlayer.customName = args[0];
        ChatUtil.sendNoSpamMessage("Set FakePlayer name to "+ args[0]);
    }

    @Override
    public String getSyntax() {
        return "fpn <name>";
    }

}

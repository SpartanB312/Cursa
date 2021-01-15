package com.deneb.client.command.commands;

import com.deneb.client.command.Command;
import com.deneb.client.features.ModuleManager;
import com.deneb.client.utils.ChatUtil;

/**
 * Created by B_312 on 12/12/20
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "vanish", description = "Vanish riding entity.")
public class Vanish extends Command {

    @Override
    public void onCall(String s, String[] args) {
        if (args.length == 0) {
            ChatUtil.sendNoSpamMessage(getSyntax());
            return;
        }

        try {
            String key = args[0];
            if (key.equalsIgnoreCase("dismount")) ModuleManager.getModuleByName("EntityDeSync").enable();
            if (key.equalsIgnoreCase("remount")) ModuleManager.getModuleByName("EntityDeSync").disable();
        } catch (Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "vanish <dismount/remount>";
    }

}
package club.deneb.client.command.commands;

import club.deneb.client.command.Command;
import club.deneb.client.features.ModuleManager;
import club.deneb.client.utils.ChatUtil;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "toggle",description = "Toggle selected module or HUD.")
public class Toggle extends Command {

    @Override
    public void onCall(String s, String[] args) {
        try {
            ModuleManager.getModuleByName(args[0]).toggle();
        } catch(Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "toggle <modulename>";
    }

}
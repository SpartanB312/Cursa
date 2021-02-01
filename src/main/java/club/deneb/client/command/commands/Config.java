package club.deneb.client.command.commands;

import club.deneb.client.client.ConfigManager;
import club.deneb.client.command.Command;
import club.deneb.client.utils.ChatUtil;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "config", description = "Save or load config.")
public class Config extends Command {

    @Override
    public void onCall(String s, String[] args) {
        if (args[0] == null) {
            ChatUtil.sendNoSpamMessage("Missing argument &bmode&r: Choose from reload, save or path");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "save":
                this.save();
                break;
            case "load":
                this.load();
                break;
        }
    }

    @Override
    public String getSyntax() {
        return "config <save/load>";
    }

    public void load() {
        ConfigManager.loadAll();
        ChatUtil.sendNoSpamMessage("Configuration reloaded!");
    }

    public void save() {
        ConfigManager.saveAll();
        ChatUtil.sendNoSpamMessage("Configuration saved!");
    }

}

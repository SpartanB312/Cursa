package club.deneb.client.command.commands;

import club.deneb.client.features.ModuleManager;
import club.deneb.client.Deneb;
import club.deneb.client.command.Command;
import club.deneb.client.command.CommandManager;
import club.deneb.client.utils.ChatUtil;
import org.lwjgl.input.Keyboard;

/**
 * Created by B_312 on 01/15/21
 */
@Command.Info(command = "help", description = "Get helps.")
public class Help extends Command {

    @Override
    public void onCall(String s, String[] args) {
        ChatUtil.printChatMessage("\2476" + Deneb.MOD_NAME + " " + "\247a" + Deneb.VERSION);
        ChatUtil.printChatMessage("\247c" + "Made by: " + Deneb.AUTHOR);
        ChatUtil.printChatMessage("\247c" + "Github: " + Deneb.GITHUB);
        ChatUtil.printChatMessage("\247a" + "Press " + "\247c" + Keyboard.getKeyName(ModuleManager.getModuleByName("ClickGUI").getBind()) + "\247a" + " to open ClickGUI");
        ChatUtil.printChatMessage("\247a" + "Press " + "\247c" + Keyboard.getKeyName(ModuleManager.getModuleByName("HUDEditor").getBind()) + "\247a" + " to open HUDEditor");
        ChatUtil.printChatMessage("\247a" + "Use command: " + "\247e" + CommandManager.INSTANCE.cmdPrefix + "prefix <target prefix>" + "\247a" + " to set command prefix");
        ChatUtil.printChatMessage("\247a" + "List all available commands: " + "\247e" + CommandManager.INSTANCE.cmdPrefix + "commands");
    }

    @Override
    public String getSyntax() {
        return "help";
    }

}
package net.spartanb312.cursa.command.commands;

import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.client.CommandManager;
import net.spartanb312.cursa.client.ModuleManager;
import net.spartanb312.cursa.command.Command;
import net.spartanb312.cursa.common.annotations.CommandInfo;
import net.spartanb312.cursa.module.modules.client.ClickGUI;
import net.spartanb312.cursa.utils.ChatUtil;
import org.lwjgl.input.Keyboard;

/**
 * Created by B_312 on 01/15/21
 */
@CommandInfo(command = "help", description = "Get helps.")
public class Help extends Command {

    @Override
    public void onCall(String s, String[] args) {
        ChatUtil.printChatMessage("\247b" + Cursa.MOD_NAME + " " + "\247a" + Cursa.MOD_VERSION);
        ChatUtil.printChatMessage("\247c" + "Made by: " + Cursa.AUTHOR);
        ChatUtil.printChatMessage("\247c" + "Github: " + Cursa.GITHUB);
        ChatUtil.printChatMessage("\2473" + "Press " + "\247c" + Keyboard.getKeyName(ModuleManager.getModule(ClickGUI.class).bindSetting.getValue().getKeyCode()) + "\2473" + " to open ClickGUI");
        ChatUtil.printChatMessage("\2473" + "Use command: " + "\2479" + CommandManager.cmdPrefix + "prefix <target prefix>" + "\2473" + " to set command prefix");
        ChatUtil.printChatMessage("\2473" + "List all available commands: " + "\2479" + CommandManager.cmdPrefix + "commands");
    }

    @Override
    public String getSyntax() {
        return "help";
    }

}
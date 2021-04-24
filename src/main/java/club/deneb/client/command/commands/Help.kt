package club.deneb.client.command.commands

import club.deneb.client.Deneb
import club.deneb.client.command.Command
import club.deneb.client.command.CommandManager
import club.deneb.client.features.ModuleManager
import club.deneb.client.utils.ChatUtil.printChatMessage
import org.lwjgl.input.Keyboard

/**
 * Created by B_312 on 01/15/21
 */
@Command.Info(command = "help", description = "Get helps.")
class Help : Command() {
    override fun onCall(s: String, vararg args: String) {
        printChatMessage("\u00a76" + Deneb.MOD_NAME + " " + "\u00a7a" + Deneb.VERSION)
        printChatMessage("\u00a7c" + "Made by: " + Deneb.AUTHOR)
        printChatMessage("\u00a7c" + "Github: " + Deneb.GITHUB)
        printChatMessage("\u00a7a" + "Press " + "\u00a7c" + Keyboard.getKeyName(ModuleManager.getModuleByName("ClickGUI").bind) + "\u00a7a" + " to open ClickGUI")
        printChatMessage("\u00a7a" + "Press " + "\u00a7c" + Keyboard.getKeyName(ModuleManager.getModuleByName("HUDEditor").bind) + "\u00a7a" + " to open HUDEditor")
        printChatMessage("\u00a7a" + "Use command: " + "\u00a7e" + CommandManager.INSTANCE.cmdPrefix + "prefix <target prefix>" + "\u00a7a" + " to set command prefix")
        printChatMessage("\u00a7a" + "List all available commands: " + "\u00a7e" + CommandManager.INSTANCE.cmdPrefix + "commands")
    }

    override fun getSyntax(): String {
        return "help"
    }
}
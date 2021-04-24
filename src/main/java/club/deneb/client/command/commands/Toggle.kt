package club.deneb.client.command.commands

import club.deneb.client.command.Command
import club.deneb.client.features.ModuleManager
import club.deneb.client.utils.ChatUtil.sendNoSpamErrorMessage

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "toggle", description = "Toggle selected module or HUD.")
class Toggle : Command() {
    override fun onCall(s: String, vararg args: String) {
        try {
            ModuleManager.getModuleByName(args[0]).toggle()
        } catch (e: Exception) {
            sendNoSpamErrorMessage(getSyntax())
        }
    }

    override fun getSyntax(): String {
        return "toggle <module name>"
    }
}
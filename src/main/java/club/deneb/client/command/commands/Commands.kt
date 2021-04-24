package club.deneb.client.command.commands

import club.deneb.client.Deneb
import club.deneb.client.command.Command
import club.deneb.client.utils.ChatUtil.printChatMessage
import club.deneb.client.utils.ChatUtil.sendNoSpamErrorMessage

/**
 * Created by B_312 on 01/15/21
 */
@Command.Info(command = "commands", description = "Lists all commands.")
class Commands : Command() {
    override fun onCall(s: String, vararg args: String) {
        printChatMessage("\u00a7a" + "Commands:")
        try {
            for (cmd in Deneb.getINSTANCE().commandManager.commands) {
                if (cmd === this) {
                    continue
                }
                printChatMessage(
                    "\u00a7a" + cmd.getSyntax().replace("<", "\u00a7e<\u00a76")
                        .replace(">", "\u00a7e>") + "\u00a77" + " - " + cmd.description
                )
            }
        } catch (e: Exception) {
            sendNoSpamErrorMessage(getSyntax())
        }
    }

    override fun getSyntax(): String {
        return "commands"
    }
}
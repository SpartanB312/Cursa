package club.deneb.client.command.commands

import club.deneb.client.utils.ChatUtil.sendNoSpamErrorMessage
import club.deneb.client.utils.ChatUtil.sendNoSpamMessage
import club.deneb.client.utils.Utils.playAnvilHit
import club.deneb.client.utils.ChatUtil
import club.deneb.client.Deneb
import club.deneb.client.command.Command

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "prefix", description = "Set command prefix.")
class Prefix : Command() {
    override fun onCall(s: String, vararg args: String) {
        if (args.isEmpty()) {
            sendNoSpamErrorMessage("Please specify a new prefix!")
            return
        }
        Deneb.getINSTANCE().commandManager.setCmdPrefix(args[0])
        sendNoSpamMessage("Prefix set to " + ChatUtil.SECTIONSIGN + "b" + args[0] + "!")
        playAnvilHit()
    }

    override fun getSyntax(): String {
        return "prefix <char>"
    }
}
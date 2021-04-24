package club.deneb.client.command.commands

import club.deneb.client.command.Command
import club.deneb.client.utils.ChatUtil.printErrorChatMessage

/**
 * Created by B_312 on 01/03/21
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "tp", description = "Teleport you to the place you want.")
class TP : Command() {
    override fun onCall(s: String, vararg args: String) {
        if (args.size < 3) {
            printErrorChatMessage(getSyntax())
            return
        }
        try {
            val x = args[0].toInt()
            val y = args[1].toInt()
            val z = args[2].toInt()
            mc.player.setPosition(x.toDouble(), y.toDouble(), z.toDouble())
        } catch (e: Exception) {
            printErrorChatMessage(getSyntax())
        }
    }

    override fun getSyntax(): String {
        return "tp <x> <y> <z>"
    }
}
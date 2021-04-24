package club.deneb.client.command.commands

import club.deneb.client.command.Command
import club.deneb.client.utils.ChatUtil.printChatMessage
import club.deneb.client.utils.ChatUtil.sendNoSpamMessage

/**
 * Created by B_312 on 01/25/21
 */
@Command.Info(command = "b", description = "Baritone commands.")
class Baritone : Command() {

    override fun onCall(s: String, vararg args: String) {
        when (args.size) {
            1 -> {
                when {
                    args[0].equals("stop", ignoreCase = true) -> {
                        mc.player.sendChatMessage("#stop")
                    }
                    args[0].equals("help", ignoreCase = true) -> {
                        printChatMessage(".b help - Get Helps.")
                        printChatMessage(".b stop - Stop all actions.")
                        printChatMessage(".b mine <block> - Mine blocks.")
                        printChatMessage(".b follow <player> - Follow a player.")
                        printChatMessage(".b goto <x> (y) <z> - Go to a place.")
                    }
                    else -> sendNoSpamMessage(getSyntax())
                }
                return
            }
            2 -> {
                when {
                    args[0].equals("mine", ignoreCase = true) -> {
                        mc.player.sendChatMessage("#mine " + args[1])
                    }
                    args[0].equals("follow", ignoreCase = true) -> {
                        mc.player.sendChatMessage("#follow " + args[1])
                    }
                    else -> sendNoSpamMessage(getSyntax())
                }
                return
            }
            3 -> {
                if (args[0].equals("goto", ignoreCase = true)) {
                    mc.player.sendChatMessage("#goto " + args[1] + " " + args[2])
                } else sendNoSpamMessage(getSyntax())
                return
            }
            4 -> {
                if (args[0].equals("goto", ignoreCase = true)) {
                    mc.player.sendChatMessage("#goto " + args[1] + " " + args[2] + " " + args[3])
                } else sendNoSpamMessage(getSyntax())
                return
            }
            else -> sendNoSpamMessage(getSyntax())
        }
    }

    override fun getSyntax(): String {
        return "b help"
    }
}
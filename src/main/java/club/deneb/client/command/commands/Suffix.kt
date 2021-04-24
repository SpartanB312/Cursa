package club.deneb.client.command.commands

import club.deneb.client.Deneb
import club.deneb.client.command.Command
import club.deneb.client.utils.ChatUtil.sendNoSpamMessage
import java.util.*

/**
 * Created by B_312 on 01/25/21
 */
@Command.Info(command = "fpn", description = "Customize chat suffix.")
class Suffix : Command() {
    override fun onCall(s: String, vararg args: String) {
        val builder = StringBuilder()
        Arrays.stream(args).forEach { arg: String? -> builder.append(arg).append(" ") }
        Deneb.CHAT_SUFFIX = builder.toString()
        sendNoSpamMessage("Set chat suffix to $builder")
    }

    override fun getSyntax(): String {
        return "suffix <message>"
    }
}
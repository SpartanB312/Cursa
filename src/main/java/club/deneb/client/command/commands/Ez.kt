package club.deneb.client.command.commands

import club.deneb.client.command.Command
import club.deneb.client.features.modules.combat.AutoEz
import club.deneb.client.utils.ChatUtil.sendNoSpamMessage
import java.util.*

/**
 * Created by B_312 on 01/25/21
 */
@Command.Info(command = "ez", description = "Customize AutoEz message.")
class Ez : Command() {
    override fun onCall(s: String, vararg args: String) {
        val builder = StringBuilder()
        Arrays.stream(args).forEach { arg: String? -> builder.append(arg).append(" ") }
        AutoEz.ezMsg = builder.toString()
        sendNoSpamMessage("Set AutoEz message to $builder")
    }

    override fun getSyntax(): String {
        return "ez <message>"
    }
}
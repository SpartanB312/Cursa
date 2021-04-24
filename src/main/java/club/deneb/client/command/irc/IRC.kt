package club.deneb.client.command.irc

import club.deneb.client.utils.ChatUtil.printChatMessage
import club.deneb.client.utils.ChatUtil.sendNoSpamErrorMessage
import club.deneb.client.Deneb
import club.deneb.client.command.Command
import club.deneb.client.utils.Timer
import java.lang.Exception
import java.lang.StringBuilder

@Command.Info(command = "irc", description = "Send message to IRC.")
class IRC : Command() {
    private var coolDownTimer = Timer().reset()
    override fun onCall(s: String, vararg args: String) {
        if (coolDownTimer.passed(5000.0)) coolDownTimer.reset() else {
            printChatMessage("IRC cooling down,you can send msg every 5s.")
            return
        }
        try {
            val content = StringBuilder()
            for (arg in args) {
                content.append(" ").append(arg)
            }
            Deneb.getINSTANCE().ircManager.client.sendChatMessage(content.toString())
        } catch (e: Exception) {
            sendNoSpamErrorMessage(getSyntax())
        }
    }

    override fun getSyntax(): String {
        return "irc <message>"
    }
}
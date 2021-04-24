package club.deneb.client.command.irc

import club.deneb.client.utils.ChatUtil.printChatMessage
import club.deneb.client.irc.IrcChatUtil.printRawIRCMessage
import club.deneb.client.utils.ChatUtil.sendNoSpamErrorMessage
import club.deneb.client.Deneb
import club.deneb.client.command.Command
import club.deneb.client.irc.IrcChatUtil
import club.deneb.client.utils.Timer
import java.lang.Exception
import java.lang.StringBuilder

@Command.Info(command = "tell", description = "Whisper to your friend.")
class Tell : Command() {
    private var coolDownTimer = Timer().reset()
    override fun onCall(s: String, vararg args: String) {
        if (coolDownTimer.passed(5000.0)) coolDownTimer.reset() else {
            printChatMessage("IRC cooling down,you can send msg every 5s.")
            return
        }
        try {
            val content = StringBuilder()
            val name = args[0]
            for (i in 1 until args.size) {
                content.append(" ").append(args[i])
            }
            Deneb.getINSTANCE().ircManager.client.send("[TELL]$name[MSG]$content")
            printRawIRCMessage(IrcChatUtil.SECTIONSIGN + "6[ Whisper to " + name + " ] " + IrcChatUtil.SECTIONSIGN + "r" + content)
        } catch (e: Exception) {
            sendNoSpamErrorMessage(getSyntax())
        }
    }

    override fun getSyntax(): String {
        return "tell <player> <message>"
    }
}
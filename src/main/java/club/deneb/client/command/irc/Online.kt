package club.deneb.client.command.irc

import club.deneb.client.utils.ChatUtil.printChatMessage
import club.deneb.client.utils.ChatUtil.sendNoSpamErrorMessage
import club.deneb.client.Deneb
import club.deneb.client.command.Command
import club.deneb.client.utils.Timer
import java.lang.Exception

@Command.Info(command = "ol", description = "Check online IRC User")
class Online : Command() {
    private var coolDownTimer = Timer().reset()
    override fun onCall(s: String, vararg args: String) {
        if (coolDownTimer.passed(5000.0)) coolDownTimer.reset() else {
            printChatMessage("IRC cooling down,you can send msg every 5s.")
            return
        }
        try {
            Deneb.getINSTANCE().ircManager.client.send("[GETONLINE]")
        } catch (e: Exception) {
            sendNoSpamErrorMessage(getSyntax())
        }
    }

    override fun getSyntax(): String {
        return "ol"
    }
}
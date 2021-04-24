package club.deneb.client.command.commands

import club.deneb.client.command.Command
import club.deneb.client.utils.ChatUtil.sendNoSpamErrorMessage
import club.deneb.client.utils.Wrapper.player
import net.minecraft.network.play.client.CPacketChatMessage

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "say", description = "Send message to chat.")
class Send : Command() {
    override fun onCall(s: String, vararg args: String) {
        try {
            var content = ""
            for (arg in args) {
                content = "$content $arg"
            }
            player.connection.sendPacket(CPacketChatMessage(content))
        } catch (e: Exception) {
            sendNoSpamErrorMessage(getSyntax())
        }
    }

    override fun getSyntax(): String {
        return "say <message>"
    }
}
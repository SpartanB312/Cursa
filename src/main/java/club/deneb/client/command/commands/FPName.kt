package club.deneb.client.command.commands

import club.deneb.client.command.Command
import club.deneb.client.features.modules.misc.FakePlayer
import club.deneb.client.utils.ChatUtil.sendNoSpamMessage

/**
 * Created by B_312 on 01/25/21
 */
@Command.Info(command = "fpn", description = "Customize fake player name.")
class FPName : Command() {
    override fun onCall(s: String, vararg args: String) {
        FakePlayer.customName = args[0]
        sendNoSpamMessage("Set FakePlayer name to " + args[0])
    }

    override fun getSyntax(): String {
        return "fpn <name>"
    }
}
package club.deneb.client.command.commands

import club.deneb.client.command.Command
import club.deneb.client.features.ModuleManager
import club.deneb.client.features.modules.misc.EntityDeSync
import club.deneb.client.utils.ChatUtil.sendNoSpamErrorMessage
import club.deneb.client.utils.ChatUtil.sendNoSpamMessage

/**
 * Created by B_312 on 12/12/20
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "vanish", description = "Vanish riding entity.")
class Vanish : Command() {
    override fun onCall(s: String, vararg args: String) {
        if (args.isEmpty()) {
            sendNoSpamMessage(getSyntax())
            return
        }
        try {
            val key = args[0]
            if (key.equals("dismount", ignoreCase = true)) ModuleManager.getModule(EntityDeSync::class.java).enable()
            if (key.equals("remount", ignoreCase = true)) ModuleManager.getModule(EntityDeSync::class.java).disable()
        } catch (e: Exception) {
            sendNoSpamErrorMessage(getSyntax())
        }
    }

    override fun getSyntax(): String {
        return "vanish <dismount/remount>"
    }
}
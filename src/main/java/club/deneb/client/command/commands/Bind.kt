package club.deneb.client.command.commands

import club.deneb.client.command.Command
import club.deneb.client.features.ModuleManager
import club.deneb.client.features.modules.client.NullHUD
import club.deneb.client.features.modules.client.NullModule
import club.deneb.client.utils.ChatUtil
import club.deneb.client.utils.ChatUtil.sendNoSpamErrorMessage
import club.deneb.client.utils.ChatUtil.sendNoSpamMessage
import club.deneb.client.utils.Wrapper.getKey

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "bind", description = "Set module bind key.")
class Bind : Command() {

    override fun onCall(s: String, vararg args: String) {
        if (args.size == 1) {
            sendNoSpamMessage("Please specify a module.")
            return
        }
        try {
            val module = args[0]
            val rKey = args[1]
            val m = ModuleManager.getModuleByName(module)
            if (m is NullModule || m is NullHUD) {
                sendNoSpamMessage("Unknown module '$module'!")
                return
            }
            var key = getKey(rKey)
            var isNone = false
            if (rKey.equals("none", ignoreCase = true)) {
                key = 0
                isNone = true
            }
            if (key == 0 && !isNone) {
                sendNoSpamMessage("Unknown key '$rKey'!")
                return
            }
            m.bind = key
            sendNoSpamMessage("Bind for " + ChatUtil.SECTIONSIGN + "b" + m.getName() + ChatUtil.SECTIONSIGN + "r set to " + ChatUtil.SECTIONSIGN + "b" + rKey.toUpperCase())
        } catch (e: Exception) {
            sendNoSpamErrorMessage(getSyntax())
        }
    }

    override fun getSyntax(): String {
        return "bind <module> <bind>"
    }
}
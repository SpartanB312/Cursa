package club.deneb.client.irc

import club.deneb.client.utils.Utils
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TextComponentString

object IrcChatUtil {

    @JvmField
    var SECTIONSIGN = '\u00A7'

    fun printRawChatMessage(message: String) {
        if (Utils.nullCheck()) return
        Minecraft.getMinecraft().ingameGUI.chatGUI.printChatMessage(TextComponentString(message))
    }

    fun printIRCMessage(message: String) {
        try {
            printRawChatMessage(
                SECTIONSIGN.toString() + "7[" + SECTIONSIGN + "6" + "DenebIRC" + SECTIONSIGN + "7] "
                        + buildIRCMsg(message)
            )
        } catch (ignored: Exception) {
        }
    }

    @JvmStatic
    fun printRawIRCMessage(message: String) {
        printRawChatMessage(SECTIONSIGN.toString() + "7[" + SECTIONSIGN + "6" + "DenebIRC" + SECTIONSIGN + "7] " + SECTIONSIGN + "r" + message)
    }

    fun buildIRCMsg(message: String): String {
        try {
            val prefix = message.substring(0, message.indexOf("]") + 1)
            val content = message.substring(message.indexOf("]") + 1)
            return getPrefix(prefix) + SECTIONSIGN + "r" + content
        } catch (ignored: Exception) { }
        return ""
    }

    fun getPrefix(prefix: String): String {
        if (prefix == "[Server]") return SECTIONSIGN.toString() + "c" + prefix
        return if (prefix.contains("whisper ]")) SECTIONSIGN.toString() + "5" + prefix else prefix
        //Here we return default
    }
}
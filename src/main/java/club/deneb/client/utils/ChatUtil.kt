package club.deneb.client.utils

import club.deneb.client.Deneb
import net.minecraft.util.text.TextComponentString
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

object ChatUtil {
    private const val DeleteID = 94423
    @JvmField
    var SECTIONSIGN = '\u00A7'
    fun sendNoSpamMessage(message: String, messageID: Int) {
        sendNoSpamRawChatMessage(
            SECTIONSIGN.toString() + "7[" + SECTIONSIGN + "6" + Deneb.CHAT_PREFIX + SECTIONSIGN + "7] " + SECTIONSIGN + "r" + message,
            messageID
        )
    }

    @JvmStatic
    fun sendNoSpamMessage(message: String) {
        sendNoSpamRawChatMessage(SECTIONSIGN.toString() + "7[" + SECTIONSIGN + "6" + Deneb.CHAT_PREFIX + SECTIONSIGN + "7] " + SECTIONSIGN + "r" + message)
    }

    @JvmStatic
    fun sendNoSpamMessage(messages: Array<String>) {
        sendNoSpamMessage("")
        for (s in messages) sendNoSpamRawChatMessage(s)
    }

    @JvmStatic
    fun sendNoSpamErrorMessage(message: String) {
        sendNoSpamRawChatMessage(SECTIONSIGN.toString() + "7[" + SECTIONSIGN + "4" + SECTIONSIGN + "lERROR" + SECTIONSIGN + "7] " + SECTIONSIGN + "r" + message)
    }

    @JvmStatic
    fun sendNoSpamErrorMessage(message: String, messageID: Int) {
        sendNoSpamRawChatMessage(
            SECTIONSIGN.toString() + "7[" + SECTIONSIGN + "4" + SECTIONSIGN + "lERROR" + SECTIONSIGN + "7] " + SECTIONSIGN + "r" + message,
            messageID
        )
    }

    @JvmStatic
    fun sendNoSpamRawChatMessage(message: String) {
        sendSpamlessMessage(message)
    }

    @JvmStatic
    fun sendNoSpamRawChatMessage(message: String, messageID: Int) {
        sendSpamlessMessage(messageID, message)
    }

    @JvmStatic
    fun printRawChatMessage(message: String) {
        if (Utils.nullCheck()) return
        chatMessage(message)
    }

    @JvmStatic
    fun printChatMessage(message: String) {
        printRawChatMessage(SECTIONSIGN.toString() + "7[" + SECTIONSIGN + "6" + Deneb.CHAT_PREFIX + SECTIONSIGN + "7] " + SECTIONSIGN + "r" + message)
    }

    @JvmStatic
    fun printErrorChatMessage(message: String) {
        printRawChatMessage(SECTIONSIGN.toString() + "7[" + SECTIONSIGN + "4" + SECTIONSIGN + "lERROR" + SECTIONSIGN + "7] " + SECTIONSIGN + "r" + message)
    }

    @SideOnly(Side.CLIENT)
    fun sendSpamlessMessage(message: String) {
        if (Utils.nullCheck()) return
        val chat = Wrapper.minecraft.ingameGUI.chatGUI
        chat.printChatMessageWithOptionalDeletion(TextComponentString(message), DeleteID)
    }

    @SideOnly(Side.CLIENT)
    fun sendSpamlessMessage(messageID: Int, message: String) {
        if (Utils.nullCheck()) return
        val chat = Wrapper.minecraft.ingameGUI.chatGUI
        chat.printChatMessageWithOptionalDeletion(TextComponentString(message), messageID)
    }

    private fun chatMessage(message: String) {
        Wrapper.minecraft.ingameGUI.chatGUI.printChatMessage(TextComponentString(message))
    }
}
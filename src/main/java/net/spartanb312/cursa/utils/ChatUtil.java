package net.spartanb312.cursa.utils;

import net.spartanb312.cursa.Cursa;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.TextComponentString;

public class ChatUtil {

    private static final int DeleteID = 114514;

    public static char SECTIONSIGN = '\u00A7';

    public static String colored(String code) {
        return SECTIONSIGN + code;
    }

    public static void sendNoSpamMessage(String message, int messageID) {
        sendNoSpamRawChatMessage(SECTIONSIGN + "7[" + SECTIONSIGN + "b" + Cursa.MOD_NAME + SECTIONSIGN + "7] " + SECTIONSIGN + "r" + message, messageID);
    }

    public static void sendNoSpamMessage(String message) {
        sendNoSpamRawChatMessage(SECTIONSIGN + "7[" + SECTIONSIGN + "b" + Cursa.MOD_NAME + SECTIONSIGN + "7] " + SECTIONSIGN + "r" + message);
    }

    public static void sendNoSpamMessage(String[] messages) {
        sendNoSpamMessage("");
        for (String s : messages) sendNoSpamRawChatMessage(s);
    }

    public static void sendNoSpamErrorMessage(String message) {
        sendNoSpamRawChatMessage(SECTIONSIGN + "7[" + SECTIONSIGN + "4" + SECTIONSIGN + "lERROR" + SECTIONSIGN + "7] " + SECTIONSIGN + "r" + message);
    }

    public static void sendNoSpamErrorMessage(String message, int messageID) {
        sendNoSpamRawChatMessage(SECTIONSIGN + "7[" + SECTIONSIGN + "4" + SECTIONSIGN + "lERROR" + SECTIONSIGN + "7] " + SECTIONSIGN + "r" + message, messageID);
    }

    public static void sendNoSpamRawChatMessage(String message) {
        sendSpamlessMessage(message);
    }

    public static void sendNoSpamRawChatMessage(String message, int messageID) {
        sendSpamlessMessage(messageID, message);
    }

    public static void printRawChatMessage(String message) {
        if (Minecraft.getMinecraft().player == null) return;
        ChatMessage(message);
    }

    public static void printChatMessage(String message) {
        printRawChatMessage(SECTIONSIGN + "7[" + SECTIONSIGN + "b" + Cursa.MOD_NAME + SECTIONSIGN + "7] " + SECTIONSIGN + "r" + message);
    }

    public static void printErrorChatMessage(String message) {
        printRawChatMessage(SECTIONSIGN + "7[" + SECTIONSIGN + "4" + SECTIONSIGN + "lERROR" + SECTIONSIGN + "7] " + SECTIONSIGN + "r" + message);
    }

    public static void sendSpamlessMessage(String message) {
        if (Minecraft.getMinecraft().player == null) return;
        final GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        chat.printChatMessageWithOptionalDeletion(new TextComponentString(message), DeleteID);
    }

    public static void sendSpamlessMessage(int messageID, String message) {
        if (Minecraft.getMinecraft().player == null) return;
        final GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        chat.printChatMessageWithOptionalDeletion(new TextComponentString(message), messageID);
    }

    private static void ChatMessage(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(message));
    }
}

package club.deneb.client.irc;

import club.deneb.client.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class IrcChatUtil {

    public static char SECTIONSIGN = '\u00A7';

    public static void printRawChatMessage(String message) {
        if (Utils.nullCheck()) return;
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new TextComponentString(message));
    }

    public static void printIRCMessage(String message){
        try {
            printRawChatMessage(SECTIONSIGN + "7[" + SECTIONSIGN + "6" + "EridanusIRC" + SECTIONSIGN +"7] "
                    + buildIRCMsg(message)
            );
        } catch (Exception ignored){}
    }

    public static void printRawIRCMessage(String message){
        printRawChatMessage(SECTIONSIGN + "7[" + SECTIONSIGN + "6" + "EridanusIRC" + SECTIONSIGN +"7] " + SECTIONSIGN + "r" + message);
    }

    public static String buildIRCMsg(String message){
        try {
            String prefix = message.substring(0, message.indexOf("]") + 1);
            String content = message.substring(message.indexOf("]") + 1);
            return getPrefix(prefix) + SECTIONSIGN + "r" + content;
        } catch (Exception ignored){}
        return "";
    }

    public static String getPrefix(String prefix){
        if(prefix.equals("[Server]")) return SECTIONSIGN + "c" + prefix;
        if(prefix.contains("whisper ]")) return SECTIONSIGN + "5" + prefix;
        return prefix;//Here we return default
    }

}

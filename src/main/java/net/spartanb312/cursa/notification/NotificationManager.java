package net.spartanb312.cursa.notification;

import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.utils.ChatUtil;

public class NotificationManager {

    public static void raw(String message) {
        ChatUtil.printChatMessage(message);
    }

    public static void info(String message) {
        raw("[Info]" + message);
    }

    public static void warn(String message) {
        raw(color("6") + "[Warning]" + color("r") + message);
    }

    public static void error(String message) {
        ChatUtil.printErrorChatMessage(color("c") + "[Error]" + color("r") + message);
    }

    public static void fatal(String message) {
        ChatUtil.printErrorChatMessage(color("4") + "[Fatal]" + color("r") + message);
    }

    public static void debug(String message) {
        raw(color("a") + "[Debug]" + color("r") + message);
    }

    public static void moduleToggle(Module module, boolean toggled) {
        ChatUtil.sendNoSpamMessage(module.name + " has been " + (toggled ? color("aEnabled") : color("cDisabled")) + color("r") + "!");
    }

    public static String color(String color) {
        return ChatUtil.SECTIONSIGN + color;
    }

}

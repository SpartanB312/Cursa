package net.spartanb312.cursa.command.commands;

import net.spartanb312.cursa.client.ModuleManager;
import net.spartanb312.cursa.command.Command;
import net.spartanb312.cursa.common.annotations.CommandInfo;
import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.utils.ChatUtil;
import org.lwjgl.input.Keyboard;

@CommandInfo(command = "bind", description = "Set module bind key.")
public class Bind extends Command {

    @Override
    public void onCall(String s, String[] args) {

        if (args.length == 1) {
            ChatUtil.sendNoSpamMessage("Please specify a module.");
            return;
        }

        try {
            String module = args[0];
            String rKey = args[1];

            Module m = ModuleManager.getModuleByName(module);

            if (m == null) {
                ChatUtil.sendNoSpamMessage("Unknown module '" + module + "'!");
                return;
            }

            if (rKey == null) {
                ChatUtil.sendNoSpamMessage(m.name + " is bound to " + ChatUtil.SECTIONSIGN + "b" + Keyboard.getKeyName(m.bindSetting.getValue().getKeyCode()));
                return;
            }

            int key = Keyboard.getKeyIndex(rKey);
            boolean isNone = false;

            if (rKey.equalsIgnoreCase("none")) {
                key = 0;
                isNone = true;
            }

            if (key == 0 && !isNone) {
                ChatUtil.sendNoSpamMessage("Unknown key '" + rKey + "'!");
                return;
            }

            m.bindSetting.getValue().setKeyCode(key);
            ChatUtil.sendNoSpamMessage("Bind for " + ChatUtil.SECTIONSIGN + "b" + m.name + ChatUtil.SECTIONSIGN + "r set to " + ChatUtil.SECTIONSIGN + "b" + rKey.toUpperCase());

        } catch (Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }

    }

    @Override
    public String getSyntax() {
        return "bind <module> <bind>";
    }

}

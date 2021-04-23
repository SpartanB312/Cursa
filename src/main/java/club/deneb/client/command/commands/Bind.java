package club.deneb.client.command.commands;


import club.deneb.client.features.AbstractModule;
import club.deneb.client.features.ModuleManager;
import club.deneb.client.features.modules.client.NullHUD;
import club.deneb.client.features.modules.client.NullModule;
import club.deneb.client.utils.ChatUtil;
import club.deneb.client.utils.Wrapper;
import club.deneb.client.command.Command;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "bind", description = "Set module bind key.")
public class Bind extends Command {

    @Override
    public void onCall(String s, String[] args) {
        if (args.length == 1) {
            ChatUtil.sendNoSpamMessage("Please specify a module.");
            return;
        }

        try {
            String module = args[0];
            String rkey = args[1];

            AbstractModule m = ModuleManager.getModuleByName(module);

            if ((m instanceof NullModule) || (m instanceof NullHUD)) {
                ChatUtil.sendNoSpamMessage("Unknown module '" + module + "'!");
                return;
            }

            if (rkey == null) {
                ChatUtil.sendNoSpamMessage(m.getName() + " is bound to " + ChatUtil.SECTIONSIGN + "b" + m.getBind());
                return;
            }

            int key = Wrapper.getKey(rkey);
            boolean isNone = false;

            if (rkey.equalsIgnoreCase("none")) {
                key = 0;
                isNone = true;
            }

            if (key == 0 && !isNone) {
                ChatUtil.sendNoSpamMessage("Unknown key '" + rkey + "'!");
                return;
            }

            m.setBind(key);
            ChatUtil.sendNoSpamMessage("Bind for " + ChatUtil.SECTIONSIGN + "b" + m.getName() + ChatUtil.SECTIONSIGN + "r set to " + ChatUtil.SECTIONSIGN + "b" + rkey.toUpperCase());

        } catch (Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "bind <module> <bind>";
    }

}

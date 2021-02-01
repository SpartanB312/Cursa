package club.deneb.client.command.commands;

import club.deneb.client.command.Command;
import club.deneb.client.features.modules.combat.AutoEz;
import club.deneb.client.utils.ChatUtil;

import java.util.Arrays;

/**
 * Created by B_312 on 01/25/21
 */
@Command.Info(command = "ez", description = "Customize AutoEz message.")
public class Ez extends Command {

    @Override
    public void onCall(String s, String[] args) {
        if (args[0] == null) {
            ChatUtil.sendNoSpamMessage(getSyntax());
            return;
        }
        StringBuilder builder = new StringBuilder();
        Arrays.stream(args).forEach(arg -> builder.append(arg).append(" "));
        AutoEz.ezMsg= builder.toString();
        ChatUtil.sendNoSpamMessage("Set AutoEz message to "+ builder.toString());
    }

    @Override
    public String getSyntax() {
        return "ez <message>";
    }

}

package club.deneb.client.command.commands;

import club.deneb.client.Deneb;
import club.deneb.client.command.Command;
import club.deneb.client.utils.ChatUtil;

/**
 * Created by B_312 on 01/15/21
 */
@Command.Info(command = "commands", description = "Lists all commands.")
public class Commands extends Command {

    @Override
    public void onCall(String s, String[] args) {
        ChatUtil.printChatMessage("\247a" + "Commands:");
        try {
            for (Command cmd : Deneb.getINSTANCE().getCommandManager().commands) {
                if (cmd == this) {
                    continue;
                }
                ChatUtil.printChatMessage("\247a" + cmd.getSyntax().replace("<", "\247e<\2476").replace(">", "\247e>") + "\2477" + " - " + cmd.getDescription());
            }
        } catch (Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "commands";
    }

}
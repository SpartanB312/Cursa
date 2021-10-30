package net.spartanb312.cursa.command.commands;


import net.spartanb312.cursa.client.CommandManager;
import net.spartanb312.cursa.command.Command;
import net.spartanb312.cursa.common.annotations.CommandInfo;
import net.spartanb312.cursa.utils.ChatUtil;
import net.spartanb312.cursa.utils.SoundUtil;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@CommandInfo(command = "prefix", description = "Set command prefix.")
public class Prefix extends Command {

    @Override
    public void onCall(String s, String[] args) {
        if (args.length <= 0) {
            ChatUtil.sendNoSpamErrorMessage("Please specify a new prefix!");
            return;
        }
        if (args[0] != null) {
            CommandManager.cmdPrefix = args[0];
            ChatUtil.sendNoSpamMessage("Prefix set to " + ChatUtil.SECTIONSIGN + "b" + args[0] + "!");
            SoundUtil.playButtonClick();
        }
    }

    @Override
    public String getSyntax() {
        return "prefix <char>";
    }

}

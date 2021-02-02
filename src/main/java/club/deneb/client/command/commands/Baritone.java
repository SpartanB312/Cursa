package club.deneb.client.command.commands;

import club.deneb.client.command.Command;
import club.deneb.client.utils.ChatUtil;


/**
 * Created by B_312 on 01/25/21
 */
@Command.Info(command = "b", description = "Baritone commands.")
public class Baritone extends Command {


    @Override
    public void onCall(String s, String[] args) {
        if (args[0] == null) {
            ChatUtil.sendNoSpamMessage(getSyntax());
            return;
        }
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("stop")){
                mc.player.sendChatMessage("#stop");
            } else if(args[0].equalsIgnoreCase("help")){
                ChatUtil.printChatMessage(".b help - Get Helps.");
                ChatUtil.printChatMessage(".b stop - Stop all actions.");
                ChatUtil.printChatMessage(".b mine <block> - Mine blocks.");
                ChatUtil.printChatMessage(".b follow <player> - Follow a player.");
                ChatUtil.printChatMessage(".b goto <x> (y) <z> - Go to a place.");

            } else ChatUtil.sendNoSpamMessage(getSyntax());
            return;
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("mine")){
                mc.player.sendChatMessage("#mine " + args[1]);
            } else if(args[0].equalsIgnoreCase("follow")){
                mc.player.sendChatMessage("#follow " + args[1]);
            } else ChatUtil.sendNoSpamMessage(getSyntax());
            return;
        } else if(args.length == 3){
            if(args[0].equalsIgnoreCase("goto")){
                mc.player.sendChatMessage("#goto " + args[1] + " " + args[2]);
            } else ChatUtil.sendNoSpamMessage(getSyntax());
            return;
        } else if(args.length == 4){
            if(args[0].equalsIgnoreCase("goto")){
                mc.player.sendChatMessage("#goto " + args[1] + " " + args[2] + " " + args[3]);
            } else ChatUtil.sendNoSpamMessage(getSyntax());
            return;
        }

        ChatUtil.sendNoSpamMessage(getSyntax());
    }

    @Override
    public String getSyntax() {
        return "b help";
    }



}

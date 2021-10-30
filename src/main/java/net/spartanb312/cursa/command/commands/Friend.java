package net.spartanb312.cursa.command.commands;

import net.spartanb312.cursa.client.FriendManager;
import net.spartanb312.cursa.command.Command;
import net.spartanb312.cursa.common.annotations.CommandInfo;
import net.spartanb312.cursa.utils.ChatUtil;
import net.spartanb312.cursa.utils.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 04/30/21
 */
@CommandInfo(command = "friend", description = "Friend command.")
public class Friend extends Command {

    @Override
    public void onCall(String s, String[] args) {
        try {
            if (args[0].equalsIgnoreCase("all")) {
                for (EntityPlayer player : Minecraft.getMinecraft().world.playerEntities) {
                    if (EntityUtil.isFakeLocalPlayer(player)) {
                        continue;
                    }
                    if (!player.isInvisible()) {
                        FriendManager.add(player);
                    }
                }
            } else if (args[0].equalsIgnoreCase("get")) {
                ChatUtil.sendNoSpamMessage(FriendManager.getInstance().friends.toString());
            } else if (args[0].equalsIgnoreCase("add")) {
                FriendManager.add(args[1]);
                ChatUtil.printChatMessage("Added friend : " + args[1]);
            } else if (args[0].equalsIgnoreCase("remove")) {
                FriendManager.remove(args[1]);
                ChatUtil.printChatMessage("Removed friend : " + args[1]);
            } else {
                ChatUtil.sendNoSpamErrorMessage(getSyntax());
            }

        } catch (Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "friend <add/all/get/remove>";
    }

}

package com.deneb.client.command.commands;

import com.deneb.client.client.FriendManager;
import com.deneb.client.command.Command;
import com.deneb.client.utils.ChatUtil;
import com.deneb.client.utils.EntityUtil;
import com.deneb.client.utils.Wrapper;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "friend", description = "Friend command.")
public class Friend extends Command {

    @Override
    public void onCall(String s, String[] args) {
        try {
            if (args[0].equalsIgnoreCase("all")) {
                for (Object object : EntityUtil.getEntityList()) {
                    if (object instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) object;
                        if (EntityUtil.isFakeLocalPlayer(player)) {
                            continue;
                        }
                        if (!player.isInvisible()) {
                            FriendManager.addFriend(EntityUtil.getPlayerName(player));
                        }
                    }
                }
            } else if (args[0].equalsIgnoreCase("get")) {
                ChatUtil.sendNoSpamMessage(FriendManager.getFriendStringList().toString());
            } else if (args[0].equalsIgnoreCase("add")) {
                boolean addedFriend = false;
                for (EntityPlayer player : Wrapper.getMinecraft().world.playerEntities) {
                    if (!player.getName().toLowerCase().equals(args[1].toLowerCase())) {
                        continue;
                    }
                    FriendManager.addFriend(player.getName());
                    addedFriend = true;
                }
                if (!addedFriend) {
                    ChatUtil.sendNoSpamErrorMessage("Player " + ChatUtil.SECTIONSIGN + "b" + args[1] + ChatUtil.SECTIONSIGN + "r" + " does not exist");
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                FriendManager.removeFriend(args[1]);
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

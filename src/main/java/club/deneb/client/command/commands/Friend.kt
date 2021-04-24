package club.deneb.client.command.commands

import club.deneb.client.client.FriendManager.addFriend
import club.deneb.client.client.FriendManager.friendList
import club.deneb.client.client.FriendManager.removeFriend
import club.deneb.client.command.Command
import club.deneb.client.utils.ChatUtil
import club.deneb.client.utils.ChatUtil.sendNoSpamErrorMessage
import club.deneb.client.utils.ChatUtil.sendNoSpamMessage
import club.deneb.client.utils.EntityUtil
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "friend", description = "Friend command.")
class Friend : Command() {
    override fun onCall(s: String, vararg args: String) {
        try {
            if (args[0].equals("all", ignoreCase = true)) {
                for (objects in EntityUtil.getEntityList()) {
                    if (objects is EntityPlayer) {
                        if (EntityUtil.isFakeLocalPlayer(objects)) {
                            continue
                        }
                        if (!objects.isInvisible) {
                            addFriend(EntityUtil.getPlayerName(objects))
                        }
                    }
                }
            } else if (args[0].equals("get", ignoreCase = true)) {
                sendNoSpamMessage(friendList().toString())
            } else if (args[0].equals("add", ignoreCase = true)) {
                var addedFriend = false
                for (player in Minecraft.getMinecraft().world.playerEntities) {
                    if (!player.name.equals(args[1], ignoreCase = true)) {
                        continue
                    }
                    addFriend(player.name)
                    addedFriend = true
                }
                if (!addedFriend) {
                    sendNoSpamErrorMessage("Player " + ChatUtil.SECTIONSIGN + "b" + args[1] + ChatUtil.SECTIONSIGN + "r" + " does not exist")
                }
            } else if (args[0].equals("remove", ignoreCase = true)) {
                removeFriend(args[1])
            } else {
                sendNoSpamErrorMessage(getSyntax())
            }
        } catch (e: Exception) {
            sendNoSpamErrorMessage(getSyntax())
        }
    }

    override fun getSyntax(): String {
        return "friend <add/all/get/remove>"
    }
}
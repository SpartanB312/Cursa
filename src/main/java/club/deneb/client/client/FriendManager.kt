package club.deneb.client.client

import club.deneb.client.utils.ChatUtil
import net.minecraft.entity.Entity

/**
 * Author B_312 on 01/15/2021
 * Updated by B_312 on 04/24/2021
 */
object FriendManager {

    var friends = mutableMapOf<String,Boolean>()

    @JvmStatic
    fun addFriend(name: String) {
        when{
            isFriend(name) -> {
                ChatUtil.sendNoSpamMessage(ChatUtil.SECTIONSIGN.toString() + "b" + name + ChatUtil.SECTIONSIGN + "r has already been our friend.")
            }
            friends[name] != null && friends[name] == false -> {
                ChatUtil.sendNoSpamMessage(ChatUtil.SECTIONSIGN.toString() + "b" + name + ChatUtil.SECTIONSIGN + "r is our friend again.")
            }
            else -> {
                ChatUtil.sendNoSpamMessage(ChatUtil.SECTIONSIGN.toString() + "b" + name + ChatUtil.SECTIONSIGN + "r is our friend now.")
            }
        }
        friends[name] = true //Kotlin strong
    }

    @JvmStatic
    fun removeFriend(name: String) {
        when{
            isFriend(name) -> {
                ChatUtil.sendNoSpamMessage(ChatUtil.SECTIONSIGN.toString() + "b" + name + ChatUtil.SECTIONSIGN + "r is no longer our friend.")
            }
            friends[name] != null && friends[name] == false -> {
                ChatUtil.sendNoSpamMessage(ChatUtil.SECTIONSIGN.toString() + "b" + name + ChatUtil.SECTIONSIGN + "r is not our friend.")
            }
            else -> {
                ChatUtil.sendNoSpamMessage(ChatUtil.SECTIONSIGN.toString() + "b" + name + ChatUtil.SECTIONSIGN + "r hasn't been our friend yet.")
            }
        }
        friends[name] = false //Kotlin strong
    }

    @JvmStatic
    fun isFriend(name: String): Boolean {
        return friends[name] != null && friends[name] == true
    }

    @JvmStatic
    fun isFriend(entity: Entity): Boolean {
        return isFriend(entity.name)
    }

    fun friendList(): List<String> {
        return friends.keys.toList()
    }

}
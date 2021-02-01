package club.deneb.client.client;

import club.deneb.client.utils.ChatUtil;
import club.deneb.client.utils.clazz.Friend;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Author B_312 on 01/15/2021
 */
public class FriendManager {

    public static FriendManager INSTANCE;

    public FriendManager(){
        INSTANCE = this;
    }

    public ArrayList<Friend> friends = new ArrayList<>();

    public boolean checkExist(String name){
        for(Friend friend : friends){
            if(friend.name.equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    public Friend getFriendByName(String name){
        for(Friend friend : friends){
            if(friend.name.equalsIgnoreCase(name)) return friend;
        }
        return null;
    }

    public static void addFriend(String name) {
        if(isNull()) return;
        if(!INSTANCE.checkExist(name)) {
            INSTANCE.friends.add(new Friend(name,true));
        } else {
            INSTANCE.getFriendByName(name).isFriend = true;
        }
        ChatUtil.sendNoSpamMessage(ChatUtil.SECTIONSIGN + "b" + name + ChatUtil.SECTIONSIGN + "r has been friended.");
    }

    public static void removeFriend(String name) {
        if(isNull()) return;
        if(INSTANCE.checkExist(name)) {
            INSTANCE.getFriendByName(name).isFriend = false;
            ChatUtil.sendNoSpamMessage(ChatUtil.SECTIONSIGN + "b" + name + ChatUtil.SECTIONSIGN + "r has been unfriended.");
        } else {
            ChatUtil.sendNoSpamMessage(ChatUtil.SECTIONSIGN + "b" + name + ChatUtil.SECTIONSIGN + "r is not our friend!");
        }
    }

    public static boolean isNull(){
        return INSTANCE == null;
    }

    public static boolean isFriend(String name) {
        if(isNull()) return false;
        if(!INSTANCE.checkExist(name)) return false;
        return INSTANCE.getFriendByName(name).isFriend;
    }

    public static boolean isFriend(Entity entity){
        return isFriend(entity.getName());
    }

    public static List<Friend> getFriendList(){
        if(isNull()) return new ArrayList<>();
        return INSTANCE.friends;
    }

    public static List<String> getFriendStringList(){
        if(isNull()) return new ArrayList<>();
        List<String> stringList = new ArrayList<>();
        getFriendList().forEach(f -> stringList.add(f.name));
        return stringList;
    }

}


package net.spartanb312.cursa.client;

import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class FriendManager {

    public List<String> friends = new ArrayList<>();

    public static void init() {
        instance = new FriendManager();
        instance.friends.clear();
    }

    public static boolean isFriend(Entity entity) {
        return isFriend(entity.getName());
    }

    public static boolean isFriend(String name) {
        return getInstance().friends.contains(name);
    }

    public static void add(String name) {
        if (!getInstance().friends.contains(name)) getInstance().friends.add(name);
    }

    public static void add(Entity entity) {
        if (!getInstance().friends.contains(entity.getName())) getInstance().friends.add(entity.getName());
    }

    public static void remove(String name) {
        getInstance().friends.remove(name);
    }

    public static void remove(Entity entity) {
        getInstance().friends.remove(entity.getName());
    }

    private static FriendManager instance;

    public static FriendManager getInstance() {
        if (instance == null) instance = new FriendManager();
        return instance;
    }

}

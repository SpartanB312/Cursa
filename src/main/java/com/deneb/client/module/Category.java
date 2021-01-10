package com.deneb.client.module;

/**
 * Created by B_312 on 01/10/21
 */
public enum Category {

    HUD("HUDEditor"),
    COMBAT("Combat"),
    MISC("Misc"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    CLIENT("Client"),

    HIDDEN("Hidden");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
package net.spartanb312.cursa.module;

public enum Category {

    COMBAT("Combat", true, false),
    MISC("Misc", true, false),
    MOVEMENT("Movement", true, false),
    PLAYER("Player", true, false),
    RENDER("Render", true, false),
    CLIENT("Client", true, false),

    HUD("HUD", true, true),

    HIDDEN("Hidden", false, false);

    public String categoryName;
    public boolean visible;
    public boolean isHUD;

    Category(String categoryName, boolean visible, boolean isHUD) {
        this.categoryName = categoryName;
        this.visible = visible;
        this.isHUD = isHUD;
    }

}

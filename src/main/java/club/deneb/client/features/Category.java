package club.deneb.client.features;

/**
 * Created by B_312 on 01/10/21
 */
public enum Category {
    //Module
    COMBAT("Combat", false),
    MISC("Misc", false),
    MOVEMENT("Movement", false),
    PLAYER("Player", false),
    RENDER("Render", false),
    CLIENT("Client", false),
    //HUD
    HUD("HUD", true),
    INFO("InfoHUD", true),
    WORLD("WorldHUD", true),
    //Hidden
    HIDDEN("Hidden", false);

    private final String name;
    private final boolean isHUDCategory;

    Category(String name, boolean isHUDCategory) {
        this.name = name;
        this.isHUDCategory = isHUDCategory;
    }

    public boolean isHUD() {
        return isHUDCategory;
    }

    public String getName() {
        return name;
    }
}
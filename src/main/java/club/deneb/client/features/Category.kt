package club.deneb.client.features

enum class Category(val categoryName: String, val isHUD: Boolean) {
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

}
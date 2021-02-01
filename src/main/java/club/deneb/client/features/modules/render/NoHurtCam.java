package club.deneb.client.features.modules.render;


import club.deneb.client.features.Category;
import club.deneb.client.features.Module;

/**
 * @author 086
 */
@Module.Info(name = "NoHurtCam", category = Category.RENDER, description = "Disables the 'hurt' camera effect")
public class NoHurtCam extends Module {

    private static NoHurtCam INSTANCE;

    public NoHurtCam() {
        INSTANCE = this;
    }

    public static boolean shouldDisable() {
        return INSTANCE != null && INSTANCE.isEnabled();
    }

}

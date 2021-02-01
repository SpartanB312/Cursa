package club.deneb.client.features.modules.player;


import club.deneb.client.features.Category;
import club.deneb.client.features.Module;

/**
 * @author 086
 */
@Module.Info(name = "Fastbreak", category = Category.PLAYER, description = "Nullifies block hit delay")
public class Fastbreak extends Module {

    @Override
    public void onTick() {
        mc.playerController.blockHitDelay = 0;
    }
}

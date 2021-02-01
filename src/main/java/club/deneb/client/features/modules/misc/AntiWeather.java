package club.deneb.client.features.modules.misc;


import club.deneb.client.features.Category;
import club.deneb.client.features.Module;

/**
 * Created by 086 on 8/04/2018.
 */
@Module.Info(name = "AntiWeather", description = "Removes rain from your world", category = Category.MISC)
public class AntiWeather extends Module {

    @Override
    public void onTick() {
        if (isDisabled()) return;
        if (mc.world.isRaining())
            mc.world.setRainStrength(0);
    }
}

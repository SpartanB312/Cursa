package club.deneb.client.features.modules.render;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.ModeValue;

/**
 * Created by 086 on 9/04/2018.
 */
@Module.Info(name = "AntiFog", description = "Disables or reduces fog", category = Category.RENDER)
public class AntiFog extends Module {

    public static ModeValue mode;
    private static AntiFog INSTANCE = new AntiFog();

    @Override
    public void onInit() {
        mode = setting("Mode", new ModeValue.Mode("NoFog",true),new ModeValue.Mode("Air"));
        INSTANCE = this;
    }

    public static boolean enabled() {
        return INSTANCE.isEnabled();
    }


}

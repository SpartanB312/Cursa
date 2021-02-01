package club.deneb.client.features.modules.misc;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.ModeValue;

/**
 * Created by 086 on 8/04/2018.
 */
@Module.Info(name = "NoEntityTrace", category = Category.MISC, description = "Blocks entities from stopping you from mining")
public class NoEntityTrace extends Module {

    ModeValue mode = setting("Mode", new ModeValue.Mode("Dynamic",true),new ModeValue.Mode("Static"));

    private static NoEntityTrace INSTANCE;

    public NoEntityTrace() {
        NoEntityTrace.INSTANCE = this;
    }

    public static boolean shouldBlock() {
        return INSTANCE.isEnabled() && (INSTANCE.mode.getToggledMode().getName().equals("Static") || mc.playerController.isHittingBlock);
    }

}

package club.deneb.client.features.modules.misc;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.ModeValue;
import club.deneb.client.value.Value;

/**
 * Created by 086 on 8/04/2018.
 */
@Module.Info(name = "NoEntityTrace", category = Category.MISC, description = "Blocks entities from stopping you from mining")
public class NoEntityTrace extends Module {

    ModeValue<String> mode = setting("Mode", "Dynamic",listOf("Dynamic","Static"));

    private static NoEntityTrace INSTANCE;

    public NoEntityTrace() {
        NoEntityTrace.INSTANCE = this;
    }

    public static boolean shouldBlock() {
        return INSTANCE.isEnabled() && (INSTANCE.mode.toggled("Static") || mc.playerController.isHittingBlock);
    }

}

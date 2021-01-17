package com.deneb.client.features.modules.misc;

import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.value.MValue;

/**
 * Created by 086 on 8/04/2018.
 */
@Module.Info(name = "NoEntityTrace", category = Category.MISC, description = "Blocks entities from stopping you from mining")
public class NoEntityTrace extends Module {

    MValue mode = setting("Mode", new MValue.Mode("Dynamic",true),new MValue.Mode("Static"));

    private static NoEntityTrace INSTANCE;

    public NoEntityTrace() {
        NoEntityTrace.INSTANCE = this;
    }

    public static boolean shouldBlock() {
        return INSTANCE.isEnabled() && (INSTANCE.mode.getToggledMode().getName().equals("Static") || mc.playerController.isHittingBlock);
    }

}

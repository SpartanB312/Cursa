package com.deneb.client.features.modules.render;

import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.value.MValue;

/**
 * Created by 086 on 9/04/2018.
 */
@Module.Info(name = "AntiFog", description = "Disables or reduces fog", category = Category.RENDER)
public class AntiFog extends Module {

    public static MValue mode;
    private static AntiFog INSTANCE = new AntiFog();

    @Override
    public void onInit() {
        mode = setting("Mode", new MValue.Mode("NoFog",true),new MValue.Mode("Air"));
        INSTANCE = this;
    }

    public static boolean enabled() {
        return INSTANCE.isEnabled();
    }


}

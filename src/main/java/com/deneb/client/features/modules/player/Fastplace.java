package com.deneb.client.features.modules.player;


import com.deneb.client.features.Category;
import com.deneb.client.features.Module;

/**
 * @author 086
 */
@Module.Info(name = "Fastplace", category = Category.PLAYER, description = "Nullifies block place delay")
public class Fastplace extends Module {

    @Override
    public void onTick() {
        mc.rightClickDelayTimer = 0;
    }
}

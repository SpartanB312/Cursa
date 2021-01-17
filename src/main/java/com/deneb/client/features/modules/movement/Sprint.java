package com.deneb.client.features.modules.movement;


import com.deneb.client.features.Category;
import com.deneb.client.features.Module;

/**
 * Created by 086 on 23/08/2017.
 */
@Module.Info(name = "Sprint", description = "Automatically makes the player sprint", category = Category.MOVEMENT)
public class Sprint extends Module {

    @Override
    public void onTick() {
        try {
            if (!mc.player.collidedHorizontally && mc.player.moveForward > 0)
                mc.player.setSprinting(true);
            else
                mc.player.setSprinting(false);
        } catch (Exception ignored) {}
    }

}

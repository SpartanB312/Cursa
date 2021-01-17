package com.deneb.client.features.modules.player;


import com.deneb.client.features.Category;
import com.deneb.client.features.Module;

/**
 * Created by 086 on 24/12/2017.
 */
@Module.Info(name = "AutoJump", category = Category.PLAYER, description = "Automatically jumps if possible")
public class AutoJump extends Module {

    @Override
    public void onTick() {
        if (mc.player.isInWater() || mc.player.isInLava()) mc.player.motionY = 0.1;
        else if (mc.player.onGround) mc.player.jump();
    }

}

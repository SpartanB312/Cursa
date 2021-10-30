package net.spartanb312.cursa.module.modules.player;

import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;

@Parallel(runnable = true)
@ModuleInfo(name = "AutoJump", category = Category.PLAYER, description = "Automatically jump")
public class AutoJump extends Module {

    @Override
    public void onTick() {
        if (mc.player == null) return;
        if (mc.player.isInWater() || mc.player.isInLava()) mc.player.motionY = 0.1;
        else if (mc.player.onGround) mc.player.jump();
    }

}

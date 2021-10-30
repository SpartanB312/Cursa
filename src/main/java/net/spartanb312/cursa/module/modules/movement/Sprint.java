package net.spartanb312.cursa.module.modules.movement;

import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;

@Parallel(runnable = true)
@ModuleInfo(name = "Sprint", category = Category.MOVEMENT, description = "Automatically sprint")
public class Sprint extends Module {

    @Override
    public void onRenderTick() {
        if (mc.player == null) return;
        mc.player.setSprinting(!mc.player.collidedHorizontally && mc.player.moveForward > 0);
    }

}

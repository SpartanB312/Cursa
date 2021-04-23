package club.deneb.client.features.modules.player;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.features.ModuleManager;
import club.deneb.client.features.modules.render.Pathfind;
import club.deneb.client.utils.EntityUtil;
import club.deneb.client.value.Value;
import net.minecraft.pathfinding.PathPoint;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


/**
 * Created by 086 on 16/12/2017.
 */
@Module.Info(name = "AutoWalk", category = Category.PLAYER)
public class AutoWalk extends Module {

    Value<String> mode = setting("Mode","Forward", listOf("Forward","Backwards","Path"));

    @SubscribeEvent
    public void onInput(InputUpdateEvent event){
        switch (mode.getValue()) {
            case "Forward":
                event.getMovementInput().moveForward = 1;
                break;
            case "Backwards":
                event.getMovementInput().moveForward = -1;
                break;
            case "Path":
                if (Pathfind.points.isEmpty()) return;
                event.getMovementInput().moveForward = 1;
                if (mc.player.isInWater() || mc.player.isInLava()) mc.player.movementInput.jump = true;
                else if (mc.player.collidedHorizontally && mc.player.onGround) mc.player.jump();
                if (!ModuleManager.getModuleByName("Pathfind").isEnabled() || Pathfind.points.isEmpty()) return;
                PathPoint next = Pathfind.points.get(0);
                lookAt(next);
                break;
        }
    }

    private void lookAt(PathPoint pathPoint) {
        double[] v = EntityUtil.calculateLookAt(pathPoint.x + .5f, pathPoint.y, pathPoint.z + .5f, mc.player);
        mc.player.rotationYaw = (float) v[0];
        mc.player.rotationPitch = (float) v[1];
    }

    private static enum AutoWalkMode {
        FORWARD, BACKWARDS, PATH
    }
}

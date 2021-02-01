package club.deneb.client.features.modules.movement;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by 086 on 15/12/2017.
 */
@Module.Info(name = "NoSlowDown", category = Category.MOVEMENT)
public class NoSlowDown extends Module {

    @SubscribeEvent
    public void eventListener(InputUpdateEvent event){
        if (mc.player.isHandActive() && !mc.player.isRiding()) {
            event.getMovementInput().moveStrafe *= 5;
            event.getMovementInput().moveForward *= 5;
        }
    }

}

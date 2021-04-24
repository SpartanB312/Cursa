package club.deneb.client.features.modules.movement

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.client.event.InputUpdateEvent
import club.deneb.client.features.AbstractModule
import club.deneb.client.features.Category
import club.deneb.client.features.Module

/**
 * Created by 086 on 15/12/2017.
 */
@Module.Info(name = "NoSlowDown", description = "Make you never be slow down",category = Category.MOVEMENT)
class NoSlowDown : Module() {
    @SubscribeEvent
    fun eventListener(event: InputUpdateEvent) {
        if (mc.player.isHandActive && !mc.player.isRiding) {
            event.movementInput.moveStrafe *= 5f
            event.movementInput.moveForward *= 5f
        }
    }
}
package club.deneb.client.features.modules.player

import club.deneb.client.event.events.client.PacketEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.init.Items
import net.minecraft.init.SoundEvents
import net.minecraft.network.play.server.SPacketSoundEffect
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Module.Info(name = "AutoFish",
    description = "Automatically catch fish",
    category = Category.MISC
)
class AutoFish : Module() {
    @SubscribeEvent
    fun onPacket(event: PacketEvent.Receive) {
        if (mc.player != null && (mc.player.heldItemMainhand.getItem() === Items.FISHING_ROD || mc.player.heldItemOffhand.getItem() === Items.FISHING_ROD) && event.packet is SPacketSoundEffect && SoundEvents.ENTITY_BOBBER_SPLASH == (event.packet as SPacketSoundEffect).getSound()) {
            Thread {
                try {
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                mc.rightClickMouse()
                try {
                    Thread.sleep(200)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                mc.rightClickMouse()
            }.start()
        }
    }
}
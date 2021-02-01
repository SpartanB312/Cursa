package club.deneb.client.features.modules.player;

import club.deneb.client.event.events.client.PacketEvent;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by 086 on 22/03/2018.
 */
@Module.Info(name = "AutoFish", category = Category.MISC, description = "Automatically catch fish")
public class AutoFish extends Module {

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (mc.player != null && (mc.player.getHeldItemMainhand().getItem() == Items.FISHING_ROD || mc.player.getHeldItemOffhand().getItem() == Items.FISHING_ROD) && event.getPacket() instanceof SPacketSoundEffect && SoundEvents.ENTITY_BOBBER_SPLASH.equals(((SPacketSoundEffect) event.getPacket()).getSound())) {
            new Thread(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mc.rightClickMouse();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mc.rightClickMouse();
            }).start();
        }
    }

}

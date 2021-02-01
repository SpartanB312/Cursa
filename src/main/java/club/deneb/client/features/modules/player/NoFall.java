package club.deneb.client.features.modules.player;

import club.deneb.client.event.events.client.PacketEvent;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.utils.EntityUtil;
import club.deneb.client.value.BooleanValue;
import club.deneb.client.value.IntValue;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by 086 on 19/11/2017.
 */
@Module.Info(category = Category.PLAYER, description = "Prevents fall damage", name = "NoFall")
public class NoFall extends Module {

    BooleanValue packet = setting("Packet", false);
    BooleanValue bucket = setting("Bucket", true);
    IntValue distance = setting("Distance", 15,0,256);

    private long last = 0;

    @SubscribeEvent
    public void onSendPacket(PacketEvent.Send event){
        if (event.getPacket() instanceof CPacketPlayer && packet.getValue()) {
            ((CPacketPlayer) event.getPacket()).onGround = true;
        }
    }

    @Override
    public void onTick() {
        if (bucket.getValue() && mc.player.fallDistance >= distance.getValue() && !EntityUtil.isAboveWater(mc.player) && System.currentTimeMillis() - last > 100) {
            Vec3d posVec = mc.player.getPositionVector();
            RayTraceResult result = mc.world.rayTraceBlocks(posVec, posVec.add(0, -5.33f, 0), true, true, false);
            if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
                EnumHand hand = EnumHand.MAIN_HAND;
                if (mc.player.getHeldItemOffhand().getItem() == Items.WATER_BUCKET) hand = EnumHand.OFF_HAND;
                else if (mc.player.getHeldItemMainhand().getItem() != Items.WATER_BUCKET) {
                    for (int i = 0; i < 9; i++)
                        if (mc.player.inventory.getStackInSlot(i).getItem() == Items.WATER_BUCKET) {
                            mc.player.inventory.currentItem = i;
                            mc.player.rotationPitch = 90;
                            last = System.currentTimeMillis();
                            return;
                        }
                    return;
                }

                mc.player.rotationPitch = 90;
                mc.playerController.processRightClick(mc.player, mc.world, hand);
                last = System.currentTimeMillis();
            }
        }
    }
}

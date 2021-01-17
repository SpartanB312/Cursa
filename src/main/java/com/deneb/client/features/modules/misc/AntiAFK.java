package com.deneb.client.features.modules.misc;

import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.value.BValue;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.util.EnumHand;

import java.util.Objects;
import java.util.Random;

/**
 * Created by 086 on 16/12/2017.
 */
@Module.Info(name = "AntiAFK", category = Category.MISC, description = "Moves in order not to get kicked. (May be invisible client-sided)")
public class AntiAFK extends Module {

    BValue swing = setting("Swing", true);
    BValue turn = setting("Turn", true);

    private final Random random = new Random();

    @Override
    public void onTick() {
        if (mc.playerController.getIsHittingBlock()) return;

        if (mc.player.ticksExisted % 40 == 0 && swing.getValue())
            Objects.requireNonNull(mc.getConnection()).sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
        if (mc.player.ticksExisted % 15 == 0 && turn.getValue())
            mc.player.rotationYaw = random.nextInt(360) - 180;

        if (!(swing.getValue() || turn.getValue()) && mc.player.ticksExisted % 80 == 0) {
            mc.player.jump();
        }
    }
}

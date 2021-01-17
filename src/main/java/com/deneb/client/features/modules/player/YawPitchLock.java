package com.deneb.client.features.modules.player;

import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.value.BValue;
import com.deneb.client.value.IValue;
import com.deneb.client.value.MValue;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

@Module.Info(name = "YawPitchLock",category = Category.PLAYER,description = "Lock your camera")
public class YawPitchLock extends Module {

    MValue mode = setting("Mode", new MValue.Mode("Yaw&Pitch",true) , new MValue.Mode("Yaw",false), new MValue.Mode("Pitch",false));
    BValue yawAuto = setting("YawAuto", true);
    IValue yaw = setting("YawValue", 180,0,360);
    IValue yawSlice = setting("YawSlice", 8,1,36);
    BValue pitchAuto = setting("PitchAuto", true);
    IValue pitch = setting("PitchValue",180,0,360);
    IValue pitchSlice = setting("YawSlice", 8,1,36);

    @Override
    public void onTick() {
        if(mode.getMode("Yaw").isToggled() || mode.getMode("Yaw&Pitch").isToggled()) {
            if (yawAuto.getValue()) {
                int angle = 360 / yawSlice.getValue();
                float yaw = mc.player.rotationYaw;
                yaw = Math.round(yaw / angle) * angle;
                mc.player.rotationYaw = yaw;
                if (mc.player.isRiding()) Objects.requireNonNull(mc.player.getRidingEntity()).rotationYaw = yaw;
            } else {
                mc.player.rotationYaw = MathHelper.clamp(yaw.getValue() - 180, -180, 180);
            }
        }

        if(mode.getMode("Pitch").isToggled() || mode.getMode("Yaw&Pitch").isToggled()) {
            if (pitchAuto.getValue()) {
                int angle = 360 / pitchSlice.getValue();
                float yaw = mc.player.rotationPitch;
                yaw = Math.round(yaw / angle) * angle;
                mc.player.rotationPitch = yaw;
                if (mc.player.isRiding()) Objects.requireNonNull(mc.player.getRidingEntity()).rotationPitch = yaw;
            } else {
                mc.player.rotationPitch = MathHelper.clamp(pitch.getValue() - 180, -180, 180);
            }
        }
    }
}

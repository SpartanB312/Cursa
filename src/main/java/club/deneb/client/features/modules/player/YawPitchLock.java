package club.deneb.client.features.modules.player;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.Value;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

@Module.Info(name = "YawPitchLock",category = Category.PLAYER,description = "Lock your camera")
public class YawPitchLock extends Module {

    Value<String> mode = setting("Mode","Yaw&Pitc",listOf("Yaw&Pitch","Yaw","Pitch"));
    Value<Boolean> yawAuto = setting("YawAuto", true);
    Value<Integer> yaw = setting("YawValue", 180,0,360);
    Value<Integer> yawSlice = setting("YawSlice", 8,1,36);
    Value<Boolean> pitchAuto = setting("PitchAuto", true);
    Value<Integer> pitch = setting("PitchValue",180,0,360);
    Value<Integer> pitchSlice = setting("YawSlice", 8,1,36);

    @Override
    public void onTick() {
        if(mode.toggled("Yaw") || mode.toggled("Yaw&Pitch")) {
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

        if(mode.toggled("Pitch") || mode.toggled("Yaw&Pitch")) {
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

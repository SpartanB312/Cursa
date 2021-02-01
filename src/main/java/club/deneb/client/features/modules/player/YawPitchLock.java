package club.deneb.client.features.modules.player;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.BooleanValue;
import club.deneb.client.value.IntValue;
import club.deneb.client.value.ModeValue;
import net.minecraft.util.math.MathHelper;

import java.util.Objects;

@Module.Info(name = "YawPitchLock",category = Category.PLAYER,description = "Lock your camera")
public class YawPitchLock extends Module {

    ModeValue mode = setting("Mode", new ModeValue.Mode("Yaw&Pitch",true) , new ModeValue.Mode("Yaw",false), new ModeValue.Mode("Pitch",false));
    BooleanValue yawAuto = setting("YawAuto", true);
    IntValue yaw = setting("YawValue", 180,0,360);
    IntValue yawSlice = setting("YawSlice", 8,1,36);
    BooleanValue pitchAuto = setting("PitchAuto", true);
    IntValue pitch = setting("PitchValue",180,0,360);
    IntValue pitchSlice = setting("YawSlice", 8,1,36);

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

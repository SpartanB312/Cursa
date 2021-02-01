package club.deneb.client.features.modules.movement;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.utils.WorldTimer;
import club.deneb.client.value.BooleanValue;
import club.deneb.client.value.DoubleValue;
import club.deneb.client.value.FloatValue;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemShield;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Author unknown we may credit thi later
 * Updated by B_312 on 01/19/21
 */
@Module.Info(name = "Speed",category = Category.MOVEMENT)
public class Speed extends Module {

    DoubleValue speed = setting("Speed",1.0,0,50);
    BooleanValue airMotion = setting("InAirMotion",true);
    BooleanValue autoJump = setting("AutoJump",true);
    BooleanValue useTimer = setting("UseTimer", true);
    BooleanValue IceSpeed = setting("IceSpeed", false);
    FloatValue slipperiness = setting("Slipperiness",0.4F,0.2F,1.0F).b(IceSpeed);

    int waitCounter;

    private final WorldTimer timers = new WorldTimer();


    @SubscribeEvent
    public void onInput (InputUpdateEvent event){
        if (mc.player.isHandActive() && !mc.player.isRiding()) {
            mc.player.movementInput.moveStrafe /= 0.2f;
            mc.player.movementInput.moveForward /= 0.2f;
        }
    }

    @Override
    public void onTick() {

        if (mc.player.isRiding()) {
            return;
        }
        if (mc.player.capabilities != null) {
            if (mc.player.capabilities.isFlying || mc.player.isElytraFlying())
                return;
        }
        if (mc.player.isHandActive()) {
            if (mc.player.getHeldItem(mc.player.getActiveHand()).getItem() instanceof ItemShield) {
                if (mc.player.movementInput.moveStrafe != 0
                        || mc.player.movementInput.moveForward != 0 && mc.player.getItemInUseMaxCount() >= 8) {
                    mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, mc.player.getHorizontalFacing()));
                }
            }
        }
        if (IceSpeed.getValue()) {
            Blocks.ICE.slipperiness = slipperiness.getValue();
            Blocks.PACKED_ICE.slipperiness = slipperiness.getValue();
            Blocks.FROSTED_ICE.slipperiness = slipperiness.getValue();
        }
        if (useTimer.getValue()) {
            timers.SetOverrideSpeed(1.088f);
        }
        boolean boost = Math.abs(mc.player.rotationYawHead - mc.player.rotationYaw) < 90;

        if (mc.player.moveForward != 0.0f || mc.player.moveStrafing != 0.0f) {
            //Sprint
            if (!mc.player.isSprinting()) {
                mc.player.setSprinting(true);
            }

            if (mc.player.onGround) {

                if (waitCounter < 1) {
                    waitCounter++;
                    return;
                } else {
                    waitCounter = 0;
                }

                final float yaw = getPlayerDirection();

                if(autoJump.getValue())mc.player.jump();
                //mc.player.motionY = 0.405;
                mc.player.motionX -= MathHelper.sin(yaw) * 0.005f * speed.getValue();
                mc.player.motionZ += MathHelper.cos(yaw) * 0.005f * speed.getValue();

            } else if(airMotion.getValue()) {

                final float direction = getPlayerDirection();
                double currentSpeed = Math.sqrt(mc.player.motionX * mc.player.motionX + mc.player.motionZ * mc.player.motionZ);
                double speed = boost ? 1.0064 : 1.001;
                if (mc.player.motionY < 0) speed = 1;

                mc.player.motionX = (-Math.sin(direction) * speed) * currentSpeed;
                mc.player.motionZ = (Math.cos(direction) * speed) * currentSpeed;

            }

        } else {

            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;

        }

    }

    private static float getPlayerDirection() {

        float rotationYaw = mc.player.rotationYaw;

        if (mc.player.moveForward < 0.0f) {
            rotationYaw += 180.0f;
        }

        float forward = 1.0f;

        if (mc.player.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (mc.player.moveForward > 0.0f) {
            forward = 0.5f;
        }

        if (mc.player.moveStrafing > 0.0f) {
            rotationYaw -= 90.0f * forward;
        }

        if (mc.player.moveStrafing < 0.0f) {
            rotationYaw += 90.0f * forward;
        }

        return rotationYaw * 0.017453292f;
    }


    @Override
    public void onDisable() {
        if (useTimer.getValue()){
            timers.resetTime();
        }
        if (IceSpeed.getValue()) {
            Blocks.ICE.slipperiness = 0.98f;
            Blocks.PACKED_ICE.slipperiness = 0.98f;
            Blocks.FROSTED_ICE.slipperiness = 0.98f;
        }
    }


    @Override
    public String getHudInfo() {
        return "";
    }
}

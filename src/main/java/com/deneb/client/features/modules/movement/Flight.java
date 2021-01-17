package com.deneb.client.features.modules.movement;

import com.deneb.client.features.Category;
import com.deneb.client.features.Module;
import com.deneb.client.utils.EntityUtil;
import com.deneb.client.value.FValue;
import com.deneb.client.value.MValue;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketPlayer;

/**
 * Created by 086 on 25/08/2017.
 */
@Module.Info(category = Category.MOVEMENT, description = "Makes the player fly", name = "Flight")
public class Flight extends Module {

    FValue speed = setting("Speed", 10f,0,50f);
    MValue mode = setting("Mode", new MValue.Mode("Vanilla",true),new MValue.Mode("Static"),new MValue.Mode("Packet"));

    @Override
    public void onEnable() {
        if (mc.player == null) return;
        if ("Vanilla".equals(mode.getToggledMode().getName())) {
            mc.player.capabilities.isFlying = true;
            if (mc.player.capabilities.isCreativeMode) return;
            mc.player.capabilities.allowFlying = true;
        }
    }

    @Override
    public void onTick() {
        switch (mode.getToggledMode().getName()) {
            case "Static":
                mc.player.capabilities.isFlying = false;
                mc.player.motionX = 0;
                mc.player.motionY = 0;
                mc.player.motionZ = 0;
                mc.player.jumpMovementFactor = speed.getValue();

                if (mc.gameSettings.keyBindJump.isKeyDown())
                    mc.player.motionY += speed.getValue();
                if (mc.gameSettings.keyBindSneak.isKeyDown())
                    mc.player.motionY -= speed.getValue();
                break;
            case "Vanilla":
                mc.player.capabilities.setFlySpeed(speed.getValue() / 100f);
                mc.player.capabilities.isFlying = true;
                if (mc.player.capabilities.isCreativeMode) return;
                mc.player.capabilities.allowFlying = true;
                break;
            case "Packet":
                int angle;

                boolean forward = mc.gameSettings.keyBindForward.isKeyDown();
                boolean left = mc.gameSettings.keyBindLeft.isKeyDown();
                boolean right = mc.gameSettings.keyBindRight.isKeyDown();
                boolean back = mc.gameSettings.keyBindBack.isKeyDown();

                if (left && right) angle = forward ? 0 : back ? 180 : -1;
                else if (forward && back) angle = left ? -90 : (right ? 90 : -1);
                else {
                    angle = left ? -90 : (right ? 90 : 0);
                    if (forward) angle /= 2;
                    else if (back) angle = 180 - (angle / 2);
                }

                if (angle != -1 && (forward || left || right || back)) {
                    float yaw = mc.player.rotationYaw + angle;
                    mc.player.motionX = EntityUtil.getRelativeX(yaw) * 0.2f;
                    mc.player.motionZ = EntityUtil.getRelativeZ(yaw) * 0.2f;
                }

                mc.player.motionY = 0;
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, mc.player.posY + (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown() ? 0.0622 : 0) - (Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown() ? 0.0622 : 0), mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, false));
                mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(mc.player.posX + mc.player.motionX, mc.player.posY - 42069, mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch, true));
                break;
        }
    }

    @Override
    public void onDisable() {
        if ("Vanilla".equals(mode.getToggledMode().getName())) {
            mc.player.capabilities.isFlying = false;
            mc.player.capabilities.setFlySpeed(0.05f);
            if (mc.player.capabilities.isCreativeMode) return;
            mc.player.capabilities.allowFlying = false;
        }
    }

}

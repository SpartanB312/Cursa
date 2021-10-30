package net.spartanb312.cursa.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;

public class SoundUtil {

    public static void playButtonClick() {
        Minecraft.getMinecraft().getSoundHandler()
                .playSound(PositionedSoundRecord
                        .getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1));
    }

    public static void playAnvilHit() {
        Minecraft.getMinecraft().getSoundHandler()
                .playSound(PositionedSoundRecord
                        .getMasterRecord(SoundEvents.BLOCK_ANVIL_HIT, 1));
    }
}
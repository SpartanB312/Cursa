package club.deneb.client.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.audio.PositionedSoundRecord
import net.minecraft.init.SoundEvents

object Utils {
    fun nullCheck(): Boolean {
        return Wrapper.player == null || Wrapper.world == null
    }

    @JvmStatic
    fun playButtonClick() {
        Minecraft.getMinecraft().getSoundHandler()
            .playSound(
                PositionedSoundRecord
                    .getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1f)
            )
    }

    @JvmStatic
    fun playAnvilHit() {
        Minecraft.getMinecraft().getSoundHandler()
            .playSound(
                PositionedSoundRecord
                    .getMasterRecord(SoundEvents.BLOCK_ANVIL_HIT, 1f)
            )
    }
}
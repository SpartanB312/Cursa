package club.deneb.client.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.network.play.server.SPacketSoundEffect;

public class Utils {

    public static boolean nullCheck() {
        return (Wrapper.getPlayer() == null || Wrapper.getWorld() == null);
    }

    public static boolean isMoving(Entity e) {
        return e.motionX != 0.0 && e.motionZ != 0.0 && e.motionY != 0.0;
    }


    public static boolean isThrowable(ItemStack stack){
        Item item = stack.getItem();
        return (item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemEnderPearl || item instanceof ItemSplashPotion || item instanceof ItemLingeringPotion || item instanceof ItemFishingRod || item instanceof ItemExpBottle);
    }

    public static boolean isPlayerMovingLegit() {
        return Wrapper.mc.gameSettings.keyBindForward.isKeyDown();
    }

    public static boolean isPlayerMovingKeybind() {
        return Wrapper.mc.gameSettings.keyBindForward.isKeyDown() || Wrapper.mc.gameSettings.keyBindBack.isKeyDown() || Wrapper.mc.gameSettings.keyBindLeft.isKeyDown() || Wrapper.mc.gameSettings.keyBindRight.isKeyDown();
    }

    public static int findBlock(final Block block) {
        return findItem(new ItemStack(block).getItem());
    }

    public static int findItem(final Item item) {
        try {
            for (int i = 0; i < 9; ++i) {
                final ItemStack stack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
                if (item == stack.getItem()) {
                    return i;
                }
            }
        } catch (Exception ignored) {
        }
        return -1;
    }

    public static boolean isBobberSplash(SPacketSoundEffect soundEffect) {
        return SoundEvents.ENTITY_BOBBER_SPLASH.equals(soundEffect.getSound());
    }

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
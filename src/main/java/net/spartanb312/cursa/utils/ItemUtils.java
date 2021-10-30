package net.spartanb312.cursa.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class ItemUtils {

    public static int getItemCount(Item item) {
        int count = Minecraft.getMinecraft().player.inventory.mainInventory.stream()
                .filter(itemStack -> itemStack.getItem() == item)
                .mapToInt(ItemStack::getCount).sum();
        if (Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() == item) {
            ++count;
        }
        return count;
    }

    public static Minecraft mc = Minecraft.getMinecraft();

    public static int findItemInHotBar(Item item) {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isTool(Item item) {
        return item instanceof ItemTool
                || item instanceof ItemSword
                || item instanceof ItemHoe
                || item instanceof ItemShears;
    }

    public static void switchToSlot(int slot) {
        if (mc.player.inventory.currentItem == slot
                || slot == -1) {
            return;
        }
        //Send packet to server
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }

    public static int findBlockInHotBar(final Block block) {
        return findItemInHotBar(Item.getItemFromBlock(block));
    }

}

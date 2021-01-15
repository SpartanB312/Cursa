package com.deneb.client.command.commands;

import com.deneb.client.command.Command;
import com.deneb.client.utils.ChatUtil;
import com.deneb.client.utils.Wrapper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityShulkerBox;

/**
 * Created by killRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Command.Info(command = "peek", description = "Look inside the contents of a shulker box without opening it.")
public class Peek extends Command {

    public static TileEntityShulkerBox sb;

    @Override
    public void onCall(String s, String[] args) {
        ItemStack is = Wrapper.getPlayer().inventory.getCurrentItem();
        try {
            if (is.getItem() instanceof ItemShulkerBox) {
                TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
                entityBox.blockType = ((ItemShulkerBox) is.getItem()).getBlock();
                entityBox.setWorld(Wrapper.getWorld());
                entityBox.readFromNBT(is.getTagCompound().getCompoundTag("BlockEntityTag"));
                sb = entityBox;
            } else {
                ChatUtil.sendNoSpamErrorMessage("You aren't carrying a shulker box.");
            }
        } catch (Exception e) {
            ChatUtil.sendNoSpamErrorMessage(getSyntax());
        }
    }

    @Override
    public String getSyntax() {
        return "peek";
    }

}

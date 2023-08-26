package net.spartanb312.cursa.module.modules.player;

import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.math.BlockPos;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.event.events.network.PacketEvent;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;

@Parallel
@ModuleInfo(name = "AntiContainer", category = Category.PLAYER, description = "Avoiding opening containers")
public class AntiContainer extends Module {

    Setting<Boolean> Chest = setting("Chest", true);
    Setting<Boolean> EnderChest = setting("EnderChest", true);
    Setting<Boolean> Trapped_Chest = setting("Trapped Chest", true);
    Setting<Boolean> Hopper = setting("Hopper", true);
    Setting<Boolean> Dispenser = setting("Dispenser", true);
    Setting<Boolean> Furnace = setting("Furnace", true);
    Setting<Boolean> Beacon = setting("Beacon", true);
    Setting<Boolean> Crafting_Table = setting("Crafting Table", true);
    Setting<Boolean> Anvil = setting("Anvil", true);
    Setting<Boolean> Enchanting_table = setting("Enchanting table", true);
    Setting<Boolean> Brewing_Stand = setting("Brewing Stand", true);
    Setting<Boolean> ShulkerBox = setting("ShulkerBox", true);

    @Override
    public void onPacketSend(PacketEvent.Send packet) {
        if (packet.packet instanceof CPacketPlayerTryUseItemOnBlock) {
            BlockPos pos = ((CPacketPlayerTryUseItemOnBlock) packet.packet).getPos();
            if (check(pos)) packet.cancel();
        }
    }

    public boolean check(BlockPos pos) {
        return ((Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.CHEST && Chest.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST && EnderChest.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.TRAPPED_CHEST && Trapped_Chest.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.HOPPER && Hopper.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.DISPENSER && Dispenser.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.FURNACE && Furnace.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.BEACON && Beacon.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.CRAFTING_TABLE && Crafting_Table.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ANVIL && Anvil.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ENCHANTING_TABLE && Enchanting_table.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.BREWING_STAND && Brewing_Stand.getValue())
                || (Minecraft.getMinecraft().world.getBlockState(pos).getBlock() instanceof BlockShulkerBox) && ShulkerBox.getValue());
    }
}

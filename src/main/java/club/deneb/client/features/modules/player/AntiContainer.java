package club.deneb.client.features.modules.player;

import club.deneb.client.event.events.client.PacketEvent;
import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.BooleanValue;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Info(name = "AntiContainer",category = Category.PLAYER)
public class AntiContainer extends Module {

    BooleanValue Chest = setting("Chest",true);
    BooleanValue EnderChest = setting("EnderChest",true);
    BooleanValue Trapped_Chest = setting("Trapped_Chest",true);
    BooleanValue Hopper = setting("Hopper",true);
    BooleanValue Dispenser = setting("Dispenser",true);
    BooleanValue Furnace = setting("Furnace",true);
    BooleanValue Beacon = setting("Beacon",true);
    BooleanValue Crafting_Table = setting("Crafting_Table",true);
    BooleanValue Anvil = setting("Anvil",true);
    BooleanValue Enchanting_table = setting("Enchanting_table",true);
    BooleanValue Brewing_Stand = setting("Brewing_Stand",true);
    BooleanValue ShulkerBox = setting("ShulkerBox",true);

    @SubscribeEvent
    public void onCheck(PacketEvent.Send packet){
        if(packet.packet instanceof CPacketPlayerTryUseItemOnBlock){
            BlockPos pos = ((CPacketPlayerTryUseItemOnBlock) packet.packet).getPos();
            if(check(pos)) packet.setCanceled(true);
        }
    }

    public boolean check(BlockPos pos){
        return ((Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.CHEST && Chest.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST && EnderChest.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.TRAPPED_CHEST && Trapped_Chest.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.HOPPER && Hopper.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.DISPENSER && Dispenser.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.FURNACE && Furnace.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.BEACON && Beacon.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.CRAFTING_TABLE && Crafting_Table.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ANVIL && Anvil.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.ENCHANTING_TABLE && Enchanting_table.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() == Blocks.BREWING_STAND && Brewing_Stand.getValue())
                ||(Minecraft.getMinecraft().world.getBlockState(pos).getBlock() instanceof BlockShulkerBox) && ShulkerBox.getValue());
    }
}

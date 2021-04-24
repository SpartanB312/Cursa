package club.deneb.client.features.modules.player

import club.deneb.client.event.events.client.PacketEvent
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
import net.minecraft.util.math.BlockPos
import net.minecraft.client.Minecraft
import net.minecraft.init.Blocks
import net.minecraft.block.BlockShulkerBox

@Module.Info(
    name = "AntiContainer",
    description = "Prevent you from opening container",
    category = Category.PLAYER
)
class AntiContainer : Module() {
    private val chest = setting("Chest", true)
    private val enderChest = setting("EnderChest", true)
    private val trappedChest = setting("Trapped_Chest", true)
    private val hopper = setting("Hopper", true)
    private val dispenser = setting("Dispenser", true)
    private val furnace = setting("Furnace", true)
    private val beacon = setting("Beacon", true)
    private val craftingTable = setting("Crafting_Table", true)
    private val anvil = setting("Anvil", true)
    private val enchantingTable = setting("Enchanting_table", true)
    private val brewingStand = setting("Brewing_Stand", true)
    private val shulkerBox = setting("ShulkerBox", true)

    @SubscribeEvent
    fun onCheck(packet: PacketEvent.Send) {
        if (packet.packet is CPacketPlayerTryUseItemOnBlock) {
            val pos = (packet.packet as CPacketPlayerTryUseItemOnBlock).pos
            if (check(pos)) packet.isCanceled = true
        }
    }

    private fun check(pos: BlockPos): Boolean {
        return (Minecraft.getMinecraft().world.getBlockState(pos).block === Blocks.CHEST && chest.value
                || Minecraft.getMinecraft().world.getBlockState(pos).block === Blocks.ENDER_CHEST && enderChest.value
                || Minecraft.getMinecraft().world.getBlockState(pos).block === Blocks.TRAPPED_CHEST && trappedChest.value
                || Minecraft.getMinecraft().world.getBlockState(pos).block === Blocks.HOPPER && hopper.value
                || Minecraft.getMinecraft().world.getBlockState(pos).block === Blocks.DISPENSER && dispenser.value
                || Minecraft.getMinecraft().world.getBlockState(pos).block === Blocks.FURNACE && furnace.value
                || Minecraft.getMinecraft().world.getBlockState(pos).block === Blocks.BEACON && beacon.value
                || Minecraft.getMinecraft().world.getBlockState(pos).block === Blocks.CRAFTING_TABLE && craftingTable.value
                || Minecraft.getMinecraft().world.getBlockState(pos).block === Blocks.ANVIL && anvil.value
                || Minecraft.getMinecraft().world.getBlockState(pos).block === Blocks.ENCHANTING_TABLE && enchantingTable.value
                || Minecraft.getMinecraft().world.getBlockState(pos).block === Blocks.BREWING_STAND && brewingStand.value
                || Minecraft.getMinecraft().world.getBlockState(pos).block is BlockShulkerBox && shulkerBox.value)
    }
}
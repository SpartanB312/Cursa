package club.deneb.client.features.modules.misc

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.features.ModuleManager
import club.deneb.client.features.modules.combat.ShulkerRegear
import net.minecraft.block.BlockShulkerBox
import net.minecraft.util.EnumHand
import net.minecraft.item.ItemPickaxe
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos

@Module.Info(name = "ShulkerNuker",
    description = "Nuker for shulker",
    category = Category.MISC
)
class ShulkerNuker : Module() {

    private val autoEnable = setting("AutoEnableRegear",false)
    var pickaxeSlot = 0

    override fun onTick() {

        if(ShulkerRegear.shouldPauseOffhand())return

        val blocks = BlockPos.getAllInBox(
            mc.player.position.add(-5, -5, -5),
            mc.player.position.add(5, 5, 5)
        ) as Iterable<BlockPos>

        var findBlock = false

        for (pos in blocks) {
            if (mc.world.getBlockState(pos).block is BlockShulkerBox) {
                pickaxeSlot = -1
                var i = 0
                while (i < 9 && pickaxeSlot == -1) {
                    val stack: ItemStack = mc.player.inventory.getStackInSlot(i)
                    if (stack != ItemStack.EMPTY) {
                        if (stack.getItem() is ItemPickaxe) {
                            stack.getItem() as ItemPickaxe
                            pickaxeSlot = i
                        }
                    }
                    ++i
                }
                if (pickaxeSlot != -1) {
                    mc.player.inventory.currentItem = pickaxeSlot
                }
                findBlock = true
                mc.playerController.onPlayerDamageBlock(pos, mc.player.horizontalFacing)
                mc.player.swingArm(EnumHand.MAIN_HAND)
                break
            }
        }

        if(autoEnable.value && !findBlock) ModuleManager.getModule(ShulkerRegear::class.java).enable()

    }

}
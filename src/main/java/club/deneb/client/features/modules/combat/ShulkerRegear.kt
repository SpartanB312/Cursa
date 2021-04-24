package club.deneb.client.features.modules.combat

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.features.ModuleManager
import club.deneb.client.utils.ChatUtil
import club.deneb.client.utils.CrystalUtil.getVecDistance
import club.deneb.client.utils.WaitCounter
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.inventory.ClickType
import net.minecraft.item.ItemShulkerBox
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.*
import java.util.stream.Collectors

@Module.Info(name = "ShulkerRegear", description = "Automatically place shulker around you", category = Category.COMBAT)
class ShulkerRegear : Module() {

    private val delay = setting("Delay", 1, 1, 10)

    private var stage = 0
    private var shulkerSlot = -1

    override fun onEnable() {
        shulkerSlot = -1
        stage = 0
        waitCounter.reset()
    }

    private val waitCounter = WaitCounter()

    override fun onTick() {
        when (stage) {
            0 -> {
                val slot = findShulkers()
                if (slot != -1) {
                    shulkerSlot = slot

                    val offhandEmpty = mc.player.heldItemOffhand.getItem() == Items.AIR

                    mc.playerController.windowClick(0, shulkerSlot, 0, ClickType.PICKUP, mc.player)
                    mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player)
                    if (!offhandEmpty) mc.playerController.windowClick(0, shulkerSlot, 0, ClickType.PICKUP, mc.player)

                    mc.playerController.updateController()
                    stage++
                } else {
                    stage = 0
                    disable()
                    ChatUtil.sendNoSpamErrorMessage("No shulkers in inventory!")
                }
            }
            1 -> {
                waitCounter.addTick()
                if (waitCounter.passed(delay.value)) {
                    val blocks = BlockPos.getAllInBox(
                        mc.player.position.add(-5, -5, -5),
                        mc.player.position.add(5, 5, 5)
                    ) as Iterable<BlockPos>

                    blocks.toMutableList().stream()
                        .sorted(Comparator.comparing { getVecDistance(it, mc.player) })
                        .collect(Collectors.toList()).forEach {
                            if (canPlaceBlockOnSide(it)) {
                                placeBlock(it)
                                stage++
                                return@forEach
                            }
                        }

                    stage=0
                    disable()
                    waitCounter.reset()
                }
            }
        }
    }

    private fun canPlaceBlockOnSide(pos: BlockPos): Boolean {
        val blockPos = pos.offset(EnumFacing.UP)
        val blockState = mc.world.getBlockState(blockPos)
        val bb = Blocks.BLACK_SHULKER_BOX.defaultState.getCollisionBoundingBox(mc.world, blockPos)
        return blockState.material.isReplaceable && bb != null && mc.world.checkNoEntityCollision(bb.offset(blockPos))
    }

    private fun placeBlock(pos: BlockPos) {
        val vec = Vec3d(pos).add(0.5, 0.5, 0.5).add(Vec3d(EnumFacing.UP.directionVec).scale(0.5))
        mc.playerController.processRightClickBlock(mc.player, mc.world, pos, EnumFacing.UP, vec, EnumHand.OFF_HAND)
        mc.player.swingArm(EnumHand.OFF_HAND)
    }


    private fun findShulkers(): Int {
        var slot = -1
        getInventoryAndHotbarSlots().forEach { (slot1, item) ->
            if (item.getItem() is ItemShulkerBox) {
                slot = slot1
            }
        }
        return slot
    }

    private fun getInventoryAndHotbarSlots(): Map<Int, ItemStack> {
        var current = 9
        val fullInventorySlots: MutableMap<Int, ItemStack> = HashMap()
        while (current <= 44) {
            fullInventorySlots[current] = mc.player.inventoryContainer.inventory[current]
            current++
        }
        return fullInventorySlots
    }

    companion object {
        fun shouldPauseOffhand(): Boolean {
            return ModuleManager.getModule(ShulkerRegear::class.java).isEnabled
        }
    }
}
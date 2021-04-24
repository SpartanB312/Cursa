package club.deneb.client.features.modules.player

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.client.settings.KeyBinding
import net.minecraft.item.ItemFood
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand

@Module.Info(
    name = "AutoEat",
    description = "Automatically eat when hungry",
    category = Category.PLAYER
)
class AutoEat : Module() {

    private var lastSlot = -1
    private var eating = false
    private fun isValid(stack: ItemStack, food: Int): Boolean {
        return stack.getItem() is ItemFood && 20 - food >= (stack.getItem() as ItemFood).getHealAmount(stack)
    }

    override fun onTick() {
        if (eating && !mc.player.isHandActive) {
            if (lastSlot != -1) {
                mc.player.inventory.currentItem = lastSlot
                lastSlot = -1
            }
            eating = false
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, false)
            return
        }
        if (eating) return
        val stats = mc.player.getFoodStats()
        if (isValid(mc.player.heldItemOffhand, stats.foodLevel)) {
            mc.player.setActiveHand(EnumHand.OFF_HAND)
            eating = true
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, true)
            mc.rightClickMouse()
        } else {
            for (i in 0..8) {
                if (isValid(mc.player.inventory.getStackInSlot(i), stats.foodLevel)) {
                    lastSlot = mc.player.inventory.currentItem
                    mc.player.inventory.currentItem = i
                    eating = true
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.keyCode, true)
                    mc.rightClickMouse()
                    return
                }
            }
        }
    }
}
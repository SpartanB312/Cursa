package club.deneb.client.features.modules.misc

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.block.state.IBlockState
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.EnumCreatureAttribute
import net.minecraft.init.Enchantments
import net.minecraft.item.ItemAxe
import net.minecraft.item.ItemSword
import net.minecraft.item.ItemTool
import net.minecraftforge.event.entity.player.AttackEntityEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Mouse
import kotlin.math.pow

@Module.Info(
    name = "AutoTool",
    description = "Automatically switch to the best tools when mining or attacking",
    category = Category.MISC
)
class AutoTool : Module() {

    private val switchBack = setting("Switch Back", true)
    private val timeout = setting("Timeout", 20, 1, 100).v { switchBack.value }
    private val preferTool = setting("Prefer", "Sword", listOf("Sword", "Axe", "None"))

    private var shouldMoveBack = false
    private var lastSlot = 0
    private var lastChange = 0L

    private var preSwitchBack = switchBack.value

    private fun onSettingChangeCheck(){
        if(preSwitchBack != switchBack.value) {
            preSwitchBack = switchBack.value
            if (!switchBack.value) shouldMoveBack = false
        }
    }

    @SubscribeEvent
    fun onClick(event: LeftClickBlock) {
        onSettingChangeCheck()
        if (shouldMoveBack || !switchBack.value)
            equipBestTool(mc.world.getBlockState(event.pos))
    }

    @SubscribeEvent
    fun onAttack(event: AttackEntityEvent) {
        equipBestWeapon(preferTool.value)
    }

    override fun onTick() {
        if (mc.currentScreen != null || !switchBack.value) return

        val mouse = Mouse.isButtonDown(0)
        if (mouse && !shouldMoveBack) {
            lastChange = System.currentTimeMillis()
            shouldMoveBack = true
            lastSlot = mc.player.inventory.currentItem
            mc.playerController.syncCurrentPlayItem()
        } else if (!mouse && shouldMoveBack && (lastChange + timeout.value * 10 < System.currentTimeMillis())) {
            shouldMoveBack = false
            mc.player.inventory.currentItem = lastSlot
            mc.playerController.syncCurrentPlayItem()
        }
    }

    private fun equipBestTool(blockState: IBlockState) {
        var bestSlot = -1
        var max = 0.0

        for (i in 0..8) {
            val stack = mc.player.inventory.getStackInSlot(i)
            if (stack.isEmpty) continue
            var speed = stack.getDestroySpeed(blockState)
            var eff: Int

            if (speed > 1) {
                speed += (if (EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack)
                        .also { eff = it } > 0.0
                ) eff.toDouble().pow(2.0) + 1 else 0.0).toFloat()
                if (speed > max) {
                    max = speed.toDouble()
                    bestSlot = i
                }
            }
        }
        if (bestSlot != -1) equip(bestSlot)
    }

    companion object {
        @JvmStatic
        fun equipBestWeapon(hitMode: String) {
            var bestSlot = -1
            var maxDamage = 0.0
            for (i in 0..8) {
                val stack = mc.player.inventory.getStackInSlot(i)
                if (stack.isEmpty) continue
                if (stack.getItem() !is ItemAxe && hitMode == "Axe") continue
                if (stack.getItem() !is ItemSword && hitMode == "Sword") continue

                if (stack.getItem() is ItemSword && (hitMode == "Sword" || hitMode == "None")) {
                    val damage = (stack.getItem() as ItemSword).attackDamage + EnchantmentHelper.getModifierForCreature(
                        stack,
                        EnumCreatureAttribute.UNDEFINED
                    ).toDouble()
                    if (damage > maxDamage) {
                        maxDamage = damage
                        bestSlot = i
                    }
                } else if (stack.getItem() is ItemAxe && (hitMode == "Axe" || hitMode == "None")) {
                    val damage = (stack.getItem() as ItemTool).attackDamage + EnchantmentHelper.getModifierForCreature(
                        stack,
                        EnumCreatureAttribute.UNDEFINED
                    ).toDouble()
                    if (damage > maxDamage) {
                        maxDamage = damage
                        bestSlot = i
                    }
                } else if (stack.getItem() is ItemTool) {
                    val damage = (stack.getItem() as ItemTool).attackDamage + EnchantmentHelper.getModifierForCreature(
                        stack,
                        EnumCreatureAttribute.UNDEFINED
                    ).toDouble()
                    if (damage > maxDamage) {
                        maxDamage = damage
                        bestSlot = i
                    }
                }
            }
            if (bestSlot != -1) equip(bestSlot)
        }

        private fun equip(slot: Int) {
            mc.player.inventory.currentItem = slot
            mc.playerController.syncCurrentPlayItem()
        }
    }
}
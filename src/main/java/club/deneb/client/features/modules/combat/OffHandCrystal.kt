package club.deneb.client.features.modules.combat

import club.deneb.client.utils.CrystalUtil.calculateDamage
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent
import club.deneb.client.features.ModuleManager
import club.deneb.client.features.modules.combat.ShulkerRegear.Companion.shouldPauseOffhand
import net.minecraft.init.Items
import net.minecraft.inventory.EntityEquipmentSlot
import club.deneb.client.utils.EntityUtil
import net.minecraft.entity.item.EntityEnderCrystal
import club.deneb.client.utils.Timer
import net.minecraft.inventory.ClickType
import net.minecraft.item.*
import org.lwjgl.input.Mouse
import java.lang.Exception

/**
 * Created by KillRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Module.Info(name = "OffHandCrystal", category = Category.COMBAT)
class OffHandCrystal : Module() {

    private val mode = setting("Item", "Crystal", listOf("Gap", "Crystal"))
    private val delay = setting("Delay", 0, 0, 1000)
    private val totem = setting("SwitchTotem", true)
    private val autoSwitchback = setting("SwitchBack", true)
    private val sbHealth = setting("Health", 11.0, 0.0, 36.0)
    private val autoSwitch = setting("SwitchGap", true)
    private val switchMode = setting("GapWhen", "Sword", listOf("Sword", "RClick")).b(autoSwitch)
    private val elytra = setting("CheckElytra", true)
    private val holeCheck = setting("CheckHole", false)
    private val holeSwitch = setting("HoleHealth", 8.0, 0.0, 36.0).b(holeCheck)
    private val crystalCalculate = setting("CalculateDmg", true)
    private val maxSelfDmg = setting("MaxSelfDmg", 26.0, 0.0, 36.0).b(crystalCalculate)
    private var totems = 0
    private var count = 0

    @SubscribeEvent
    fun onPlayerUpdate(event: RenderTickEvent?) {

        if ((ModuleManager.getModule(AutoTotem::class.java) as AutoTotem).soft.value == false) {
            (ModuleManager.getModule(AutoTotem::class.java) as AutoTotem).soft.value = true
        }

        if (shouldPauseOffhand()) return

        if (mc.player == null) return

        var crystals = mc.player.inventory.mainInventory.stream()
            .filter { itemStack: ItemStack -> itemStack.getItem() === Items.END_CRYSTAL }
            .mapToInt { obj: ItemStack -> obj.count }.sum()

        var gApple = mc.player.inventory.mainInventory.stream()
            .filter { itemStack: ItemStack -> itemStack.getItem() === Items.GOLDEN_APPLE }
            .mapToInt { obj: ItemStack -> obj.count }.sum()

        totems = mc.player.inventory.mainInventory.stream()
            .filter { itemStack: ItemStack -> itemStack.getItem() === Items.TOTEM_OF_UNDYING }
            .mapToInt { obj: ItemStack -> obj.count }.sum()

        when {
            mc.player.heldItemOffhand.getItem() === Items.END_CRYSTAL -> {
                crystals += mc.player.heldItemOffhand.count
            }
            mc.player.heldItemOffhand.getItem() === Items.GOLDEN_APPLE -> {
                gApple += mc.player.heldItemOffhand.count
            }
            mc.player.heldItemOffhand.getItem() === Items.TOTEM_OF_UNDYING -> {
                totems++
            }
        }

        var item: Item? = null
        if (!mc.player.heldItemOffhand.isEmpty) {
            item = mc.player.heldItemOffhand.item
        }

        count = if (item != null) {
            when (item) {
                Items.END_CRYSTAL -> {
                    crystals
                }
                Items.TOTEM_OF_UNDYING -> {
                    totems
                }
                else -> {
                    gApple
                }
            }
        } else {
            0
        }

        val handItem = mc.player.heldItemMainhand.item
        val offhandItem = if (mode.toggled("Crystal")) Items.END_CRYSTAL else Items.GOLDEN_APPLE
        val sOffhandItem = if (mode.toggled("Crystal")) Items.GOLDEN_APPLE else Items.END_CRYSTAL
        val shouldSwitch = if (switchMode.toggled("Sword")) {
            mc.player.heldItemMainhand.getItem() is ItemSword && Mouse.isButtonDown(1) && autoSwitch.value
        } else {
            (Mouse.isButtonDown(1)
                    && autoSwitch.value
                    && handItem !is ItemFood
                    && handItem !is ItemExpBottle
                    && handItem !is ItemBlock)
        }
        if (shouldTotem() && getItemSlot(Items.TOTEM_OF_UNDYING) != -1) {
            switchTotem()
        } else {
            if (shouldSwitch && getItemSlot(sOffhandItem) != -1) {
                if (mc.player.heldItemOffhand.getItem() != sOffhandItem) {
                    val slot =
                        if (getItemSlot(sOffhandItem) < 9) getItemSlot(sOffhandItem) + 36 else getItemSlot(sOffhandItem)
                    switchTo(slot)
                }
            } else if (getItemSlot(offhandItem) != -1) {
                val slot = if (getItemSlot(offhandItem) < 9) getItemSlot(offhandItem) + 36 else getItemSlot(offhandItem)
                if (mc.player.heldItemOffhand.getItem() != offhandItem) {
                    switchTo(slot)
                }
            }
        }
    }

    private fun shouldTotem(): Boolean {
        return if (totem.value) {
            (checkHealth()
                    || mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST)
                .getItem() === Items.ELYTRA && elytra.value || mc.player.fallDistance >= 5.0f || EntityUtil.isPlayerInHole() && holeCheck.value && mc.player.health + mc.player.absorptionAmount <= holeSwitch.value || crystalCalculate.value && calcHealth())
        } else {
            false
        }
    }

    private fun calcHealth(): Boolean {
        var maxDmg = 0.5
        for (entity in mc.world.loadedEntityList) {
            if (entity !is EntityEnderCrystal) continue
            if (mc.player.getDistance(entity) > 12f) continue
            val d = calculateDamage(entity.posX, entity.posY, entity.posZ, mc.player).toDouble()
            if (d > maxDmg) maxDmg = d
        }
        return if (maxDmg - 0.5 > mc.player.health + mc.player.absorptionAmount) true else maxDmg > maxSelfDmg.value
    }

    private fun checkHealth(): Boolean {
        val lowHealth = mc.player.health + mc.player.absorptionAmount <= sbHealth.value
        val notInHoleAndLowHealth = lowHealth && !EntityUtil.isPlayerInHole()
        return if (holeCheck.value) notInHoleAndLowHealth else lowHealth
    }

    private fun switchTotem() {
        if (totems != 0) {
            if (mc.player.heldItemOffhand.getItem() != Items.TOTEM_OF_UNDYING) {
                val slot =
                    if (getItemSlot(Items.TOTEM_OF_UNDYING) < 9) getItemSlot(Items.TOTEM_OF_UNDYING) + 36 else getItemSlot(
                        Items.TOTEM_OF_UNDYING
                    )
                switchTo(slot)
            }
        }
    }

    private fun switchTo(slot: Int) {
        try {
            if (timer.passed(delay.value.toDouble())) {
                mc.playerController.windowClick(
                    mc.player.inventoryContainer.windowId,
                    slot,
                    0,
                    ClickType.PICKUP,
                    mc.player
                )
                mc.playerController.windowClick(
                    mc.player.inventoryContainer.windowId,
                    45,
                    0,
                    ClickType.PICKUP,
                    mc.player
                )
                mc.playerController.windowClick(
                    mc.player.inventoryContainer.windowId,
                    slot,
                    0,
                    ClickType.PICKUP,
                    mc.player
                )
                timer.reset()
            }
        } catch (ignored: Exception) {
        }
    }

    override fun onDisable() {
        if (autoSwitchback.value) {
            switchTotem()
        }
    }

    override fun getHudInfo(): String {
        return count.toString() + ""
    }

    companion object {
        var INSTANCE: OffHandCrystal? = null
        private val timer = Timer()
        private fun getItemSlot(input: Item): Int {
            var itemSlot = -1
            for (i in 45 downTo 1) {
                if (mc.player.inventory.getStackInSlot(i).getItem() !== input) continue
                itemSlot = i
                break
            }
            return itemSlot
        }
    }
}
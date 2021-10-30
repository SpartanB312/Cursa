package net.spartanb312.cursa.module.modules.combat;

import net.spartanb312.cursa.client.ModuleManager;
import net.spartanb312.cursa.common.annotations.ModuleInfo;
import net.spartanb312.cursa.common.annotations.Parallel;
import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.utils.CrystalUtil;
import net.spartanb312.cursa.utils.EntityUtil;
import net.spartanb312.cursa.utils.Timer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import org.lwjgl.input.Mouse;

/**
 * Created by KillRED in 2020
 * Updated by B_312 on 01/15/21
 */
@Parallel(runnable = true)
@ModuleInfo(name = "OffHandCrystal", category = Category.COMBAT, description = "Automatically switch crystals and totem")
public class OffHandCrystal extends Module {

    Setting<Mode> mode = setting("Item", Mode.Crystal);
    Setting<Integer> delay = setting("Delay", 0, 0, 1000);
    Setting<Boolean> totem = setting("SwitchTotem", true);
    Setting<Boolean> autoSwitchback = setting("SwitchBack", true);
    Setting<Double> sbHealth = setting("Health", 11D, 0D, 36D);
    Setting<Boolean> autoSwitch = setting("SwitchGap", true);
    Setting<SwitchMode> switchMode = setting("GapWhen", SwitchMode.Sword).whenTrue(autoSwitch);
    Setting<Boolean> elytra = setting("CheckElytra", true);
    Setting<Boolean> holeCheck = setting("CheckHole", false);
    Setting<Double> holeSwitch = setting("HoleHealth", 8d, 0d, 36d).whenTrue(holeCheck);
    Setting<Boolean> crystalCalculate = setting("CalculateDmg", true);
    Setting<Double> maxSelfDmg = setting("MaxSelfDmg", 26d, 0d, 36d).whenTrue(crystalCalculate);

    enum Mode {
        Crystal, Gap
    }

    enum SwitchMode {
        Sword, RClick
    }

    private int totems;
    private int count;
    private static final Timer timer = new Timer();

    @Override
    public void onRenderTick() {
        if (((AutoTotem) ModuleManager.getModule(AutoTotem.class)).soft.getValue().equals(false)) {
            ((AutoTotem) ModuleManager.getModule(AutoTotem.class)).soft.setValue(true);
        }

        if (mc.player == null) return;

        int crystals = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.END_CRYSTAL).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            crystals += mc.player.getHeldItemOffhand().getCount();
        }

        int gapple = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.GOLDEN_APPLE).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
            gapple += mc.player.getHeldItemOffhand().getCount();
        }

        totems = mc.player.inventory.mainInventory.stream().filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING).mapToInt(ItemStack::getCount).sum();
        if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            totems++;
        }

        Item item = null;

        if (!mc.player.getHeldItemOffhand().isEmpty()) {
            item = mc.player.getHeldItemOffhand().getItem();
        }

        if (item != null) {
            if (item.equals(Items.END_CRYSTAL)) {
                count = crystals;
            } else if (item.equals(Items.TOTEM_OF_UNDYING)) {
                count = totems;
            } else {
                count = gapple;
            }
        } else {
            count = 0;
        }

        Item handItem = mc.player.getHeldItemMainhand().getItem();
        Item offhandItem = mode.getValue().equals(Mode.Crystal) ? Items.END_CRYSTAL : Items.GOLDEN_APPLE;
        Item sOffhandItem = mode.getValue().equals(Mode.Crystal) ? Items.GOLDEN_APPLE : Items.END_CRYSTAL;

        boolean shouldSwitch;

        if (switchMode.getValue().equals(SwitchMode.Sword)) {
            shouldSwitch = mc.player.getHeldItemMainhand().getItem() instanceof ItemSword && Mouse.isButtonDown(1) && autoSwitch.getValue();
        } else {
            shouldSwitch = Mouse.isButtonDown(1)
                    && autoSwitch.getValue()
                    && !(handItem instanceof ItemFood)
                    && !(handItem instanceof ItemExpBottle)
                    && !(handItem instanceof ItemBlock);
        }


        if (shouldTotem() && getItemSlot(Items.TOTEM_OF_UNDYING) != -1) {
            switch_Totem();
        } else {
            if (shouldSwitch && getItemSlot(sOffhandItem) != -1) {
                if (!mc.player.getHeldItemOffhand().getItem().equals(sOffhandItem)) {
                    final int slot = getItemSlot(sOffhandItem) < 9 ? getItemSlot(sOffhandItem) + 36 : getItemSlot(sOffhandItem);
                    switchTo(slot);
                }
            } else if (getItemSlot(offhandItem) != -1) {
                final int slot = getItemSlot(offhandItem) < 9 ? getItemSlot(offhandItem) + 36 : getItemSlot(offhandItem);
                if (!mc.player.getHeldItemOffhand().getItem().equals(offhandItem)) {
                    switchTo(slot);
                }
            }
        }
    }

    private boolean shouldTotem() {
        if (totem.getValue()) {
            return checkHealth()
                    || mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == Items.ELYTRA && elytra.getValue()
                    || mc.player.fallDistance >= 5.0f
                    || EntityUtil.isPlayerInHole() && holeCheck.getValue() && mc.player.getHealth() + mc.player.getAbsorptionAmount() <= holeSwitch.getValue()
                    || crystalCalculate.getValue() && calcHealth();
        } else {
            return false;
        }
    }

    private boolean calcHealth() {
        double maxDmg = 0.5;
        for (Entity entity : mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            if (mc.player.getDistance(entity) > 12f) continue;
            double d = CrystalUtil.calculateDamage(entity.posX, entity.posY, entity.posZ, mc.player);
            if (d > maxDmg) maxDmg = d;
        }
        if (maxDmg - 0.5 > mc.player.getHealth() + mc.player.getAbsorptionAmount()) return true;
        return maxDmg > maxSelfDmg.getValue();
    }

    public boolean checkHealth() {
        boolean lowHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount() <= sbHealth.getValue();
        boolean notInHoleAndLowHealth = lowHealth && !EntityUtil.isPlayerInHole();
        return holeCheck.getValue() ? notInHoleAndLowHealth : lowHealth;
    }

    private void switch_Totem() {
        if (totems != 0) {
            if (!mc.player.getHeldItemOffhand().getItem().equals(Items.TOTEM_OF_UNDYING)) {
                final int slot = getItemSlot(Items.TOTEM_OF_UNDYING) < 9 ? getItemSlot(Items.TOTEM_OF_UNDYING) + 36 : getItemSlot(Items.TOTEM_OF_UNDYING);
                switchTo(slot);
            }
        }
    }

    private void switchTo(int slot) {
        try {
            if (timer.passed(delay.getValue())) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
                timer.reset();
            }
        } catch (Exception ignored) {
        }
    }

    private int getItemSlot(Item input) {
        int itemSlot = -1;
        for (int i = 45; i > 0; --i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() != input) continue;
            itemSlot = i;
            break;
        }
        return itemSlot;
    }


    @Override
    public void onDisable() {
        if (autoSwitchback.getValue()) {
            switch_Totem();
        }
    }

    @Override
    public String getModuleInfo() {
        return String.valueOf(count);
    }

}

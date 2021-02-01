package club.deneb.client.features.modules.combat;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.features.ModuleManager;
import club.deneb.client.utils.EntityUtil;
import club.deneb.client.utils.Timer;
import club.deneb.client.value.BooleanValue;
import club.deneb.client.value.DoubleValue;
import club.deneb.client.value.IntValue;
import club.deneb.client.value.ModeValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

/**
 * Created by KillRED on 2020
 * Updated by B_312 on 01/15/21
 */
@Module.Info(name = "OffHandCrystal", category = Category.COMBAT)
public class OffHandCrystal extends Module {

    public static OffHandCrystal INSTANCE;

    //Optimized redundant settings
    ModeValue mode = setting("Item", new ModeValue.Mode("Gap"), new ModeValue.Mode("Crystal", true));
    IntValue delay = setting("Delay", 0, 0, 1000);
    BooleanValue totem = setting("SwitchTotem", true);
    BooleanValue autoSwitchback = setting("SwitchBack", true);
    DoubleValue sbHealth = setting("Health", 11D, 0D, 36D);
    BooleanValue autoSwitch = setting("SwitchGap", true);
    ModeValue switchMode = setting("GapWhen", new ModeValue.Mode("Sword", true), new ModeValue.Mode("RClick", false)).b(autoSwitch);
    BooleanValue elytra = setting("CheckElytra",true);
    BooleanValue holeCheck = setting("CheckHole", false);
    DoubleValue holeSwitch = setting("HoleHealth",8d,0d,36d).b(holeCheck);
    BooleanValue crystalCalculate = setting("CalculateDmg",true);
    DoubleValue maxSelfDmg = setting("MaxSelfDmg",26d,0d,36d).b(crystalCalculate);

    private int totems;
    private int count;
    private static final Timer timer = new Timer();


    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.RenderTickEvent event){
        if (((AutoTotem) ModuleManager.getModuleByName("AutoTotem")).soft.getValue().equals(false)) {
            ((AutoTotem) ModuleManager.getModuleByName("AutoTotem")).soft.setValue(true);
        }

        if(mc.player == null) return;

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

        if (!mc.player.getHeldItemOffhand().isEmpty) {
            item = mc.player.getHeldItemOffhand().item;
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

        Item handItem = mc.player.getHeldItemMainhand().item;
        Item offhandItem = mode.getMode("Crystal").isToggled() ? Items.END_CRYSTAL : Items.GOLDEN_APPLE;
        Item sOffhandItem = mode.getMode("Crystal").isToggled() ? Items.GOLDEN_APPLE : Items.END_CRYSTAL;

        boolean shouldSwitch;

        if (switchMode.getMode("Sword").isToggled()){
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

    private boolean calcHealth(){
        double maxDmg = 0.5;
        for (Entity entity : mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            if (mc.player.getDistance(entity) > 12f) continue;
            double d = AutoCrystal.calculateDamage(entity.posX,entity.posY,entity.posZ,mc.player);
            if(d > maxDmg) maxDmg = d;
        }
        if(maxDmg - 0.5 > mc.player.getHealth() + mc.player.getAbsorptionAmount()) return true;
        return maxDmg > maxSelfDmg.getValue();
    }

    public boolean checkHealth(){
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

    private void switchTo(int slot){
        try {
            if (timer.passed(delay.getValue())) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player);
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, mc.player);
                timer.reset();
            }
        }catch (Exception ignored){}
    }

    private static int getItemSlot(Item input) {
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
    public String getHudInfo() {
        return count + "";
    }
}

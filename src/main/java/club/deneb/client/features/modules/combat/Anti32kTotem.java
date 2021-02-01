package club.deneb.client.features.modules.combat;

import club.deneb.client.features.Category;
import club.deneb.client.features.Module;
import club.deneb.client.value.BooleanValue;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;

/**
 * Created by B_312 on 06/24/19
 */
@Module.Info(name = "Anti32KTotem",category = Category.COMBAT)
public class Anti32kTotem extends Module {

    BooleanValue pauseInInventory = setting("PauseInInventory",false);

    @Override
    public void onTick() {
		
        if (mc.currentScreen != null) {
            return;
        }
		if (pauseInInventory.getValue() && mc.currentScreen instanceof GuiContainer){
			return;
		}
        if (mc.player.inventory.getStackInSlot(0).getItem() == Items.TOTEM_OF_UNDYING) {
            return;
        }
        for (int i = 9; i < 35; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.SWAP, mc.player);
                break;
            }
        }
    }
}

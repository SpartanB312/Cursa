package club.deneb.client.features.modules.combat

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.init.Items
import net.minecraft.inventory.ClickType

/**
 * Created by B_312 on 06/24/19
 */
@Module.Info(name = "Anti32KTotem", category = Category.COMBAT)
class Anti32kTotem : Module() {

    private var pauseInInventory = setting("PauseInInventory", false)

    override fun onTick() {
        if (mc.currentScreen != null
            || pauseInInventory.value && mc.currentScreen is GuiContainer
            || mc.player.inventory.getStackInSlot(0).getItem() === Items.TOTEM_OF_UNDYING
        ) return

        for (i in 9..34) {
            if (mc.player.inventory.getStackInSlot(i).getItem() === Items.TOTEM_OF_UNDYING) {
                mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.SWAP, mc.player)
                break
            }
        }
    }

}
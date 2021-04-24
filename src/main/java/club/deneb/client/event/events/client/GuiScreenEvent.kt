package club.deneb.client.event.events.client

import net.minecraft.client.gui.GuiScreen
import club.deneb.client.event.MinecraftEvent

open class GuiScreenEvent(var screen: GuiScreen) : MinecraftEvent() {
    class Displayed(screen: GuiScreen) : GuiScreenEvent(screen)
    class Closed(screen: GuiScreen) : GuiScreenEvent(screen)
}
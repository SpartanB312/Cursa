package club.deneb.client.features.modules.misc

import club.deneb.client.event.events.client.GuiScreenEvent
import club.deneb.client.event.events.client.GuiScreenEvent.Displayed
import club.deneb.client.features.Category
import club.deneb.client.features.Module
import club.deneb.client.features.ModuleManager
import club.deneb.client.value.Value
import net.minecraft.client.gui.GuiDisconnected
import net.minecraft.client.multiplayer.GuiConnecting
import net.minecraft.client.multiplayer.ServerData
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.math.floor
import kotlin.math.max

@Module.Info(
    name = "AutoReconnect",
    description = "Automatically reconnects after being disconnected",
    category = Category.MISC
)
class AutoReconnect : Module() {

    companion object {
        lateinit var seconds: Value<Int>
        private var cServer: ServerData? = null
    }

    init {
        seconds = setting("Seconds", 5, 0, 100)
        MinecraftForge.EVENT_BUS.register(AlwaysListening())
    }

    class AlwaysListening {
        @SubscribeEvent
        fun closedListener(event: GuiScreenEvent.Closed) {
            if (event.screen is GuiConnecting) cServer = mc.currentServerData
        }

        @SubscribeEvent
        fun displayedListener(event: Displayed) {
            if (ModuleManager.getModule(AutoReconnect::class.java).isEnabled && event.screen is GuiDisconnected && (cServer != null || mc.currentServerData != null)) event.screen =
                TGuiDisconnected(event.screen as GuiDisconnected)
        }
    }

    class TGuiDisconnected(disconnected: GuiDisconnected) :
        GuiDisconnected(disconnected.parentScreen, disconnected.reason, disconnected.message) {
        private var millis = seconds.value * 1000
        private var cTime: Long
        override fun updateScreen() {
            if (millis <= 0) mc.displayGuiScreen(
                GuiConnecting(
                    parentScreen,
                    mc,
                    if (cServer != null) cServer!!
                    else mc.currentServerData
                )
            )
        }

        override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
            super.drawScreen(mouseX, mouseY, partialTicks)
            val a = System.currentTimeMillis()
            millis -= (a - cTime).toInt()
            cTime = a
            val s = "Reconnecting in " + max(0.0, floor(millis.toDouble() / 100) / 10) + "s"
            fontRenderer.drawString(
                s,
                (width / 2 - fontRenderer.getStringWidth(s) / 2).toFloat(),
                (height - 16).toFloat(),
                0xffffff,
                true
            )
        }

        init {
            cTime = System.currentTimeMillis()
        }
    }
}
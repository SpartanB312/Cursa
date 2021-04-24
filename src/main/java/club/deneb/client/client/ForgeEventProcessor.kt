package club.deneb.client.client

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import club.deneb.client.features.ModuleManager
import java.lang.RuntimeException
import club.deneb.client.utils.ChatUtil
import net.minecraftforge.client.event.InputUpdateEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraft.entity.passive.AbstractHorse
import club.deneb.client.utils.DenebTessellator
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import club.deneb.client.command.commands.Peek
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.inventory.GuiShulkerBox
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.client.event.ClientChatEvent
import club.deneb.client.Deneb
import club.deneb.client.irc.IrcManager
import club.deneb.client.utils.Utils
import club.deneb.client.utils.Wrapper
import org.lwjgl.input.Keyboard
import java.lang.Exception

/**
 * Author KillRED on 2020
 * Updated by B_312 on 04/24/2021
 */
object ForgeEventProcessor {

    @SubscribeEvent
    fun onWorldRender(event: RenderWorldLastEvent) {
        if (event.isCanceled || Utils.nullCheck()) return
        try {
            ModuleManager.onWorldRender(event)
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
            ChatUtil.printErrorChatMessage("RuntimeException: onWorldRender")
            ChatUtil.printErrorChatMessage(ex.toString())
        }
    }

    @SubscribeEvent
    fun onKey(event: InputUpdateEvent?) {
        try {
            ModuleManager.onKey(event)
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
            ChatUtil.printErrorChatMessage("RuntimeException: onKey")
            ChatUtil.printErrorChatMessage(ex.toString())
        }
    }

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Post) {
        if (event.isCanceled || Utils.nullCheck()) return
        try {
            var target = RenderGameOverlayEvent.ElementType.EXPERIENCE
            if (!Wrapper.player.isCreative && Wrapper.player.getRidingEntity() is AbstractHorse) target =
                RenderGameOverlayEvent.ElementType.HEALTHMOUNT
            if (event.type == target) {
                ModuleManager.onRender(event)
                DenebTessellator.releaseGL()
            }
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
            ChatUtil.printErrorChatMessage("RuntimeException: onRender")
            ChatUtil.printErrorChatMessage(ex.toString())
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (event.isCanceled || Utils.nullCheck()) return
        try {
            ModuleManager.onTick()
            if (Peek.sb != null) {
                val scaledResolution = ScaledResolution(Minecraft.getMinecraft())
                val i = scaledResolution.scaledWidth
                val j = scaledResolution.scaledHeight
                val gui = GuiShulkerBox(Wrapper.player.inventory, Peek.sb)
                gui.setWorldAndResolution(Minecraft.getMinecraft(), i, j)
                Minecraft.getMinecraft().displayGuiScreen(gui)
                Peek.sb = null
            }
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
            ChatUtil.printErrorChatMessage("RuntimeException: onTick")
            ChatUtil.printErrorChatMessage(ex.toString())
        }
    }

    @SubscribeEvent
    fun onKeyInput(event: KeyInputEvent?) {
        if (Utils.nullCheck()) return
        try {
            if (Keyboard.getEventKeyState()) {
                ModuleManager.onBind(Keyboard.getEventKey())
            }
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
            ChatUtil.printErrorChatMessage("RuntimeException: onKeyInput")
            ChatUtil.printErrorChatMessage(ex.toString())
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onChatSent(event: ClientChatEvent) {
        if (Utils.nullCheck()) return
        try {
            if (event.message.startsWith(Deneb.getINSTANCE().commandManager.getCmdPrefix())) {
                event.isCanceled = true
                try {
                    Minecraft.getMinecraft().ingameGUI.chatGUI.addToSentMessages(event.message)
                    if (event.message.length > 1) Deneb.getINSTANCE().commandManager.runCommands(
                        event.message.substring(
                            Deneb.getINSTANCE().commandManager.getCmdPrefix().length - 1
                        )
                    ) else ChatUtil.sendNoSpamMessage("Please enter a command.")
                } catch (e: Exception) {
                    e.printStackTrace()
                    ChatUtil.sendNoSpamMessage("Command Error! Exception: [" + e.message + "]")
                }
                event.message = ""
            } else if (IrcManager.isChatSend) {
                val s = event.message
                if (s.startsWith("/")) return
                Deneb.getINSTANCE().ircManager.client.sendChatMessage(s)
                event.isCanceled = true
            }
        } catch (ex: RuntimeException) {
            ex.printStackTrace()
            ChatUtil.printErrorChatMessage("RuntimeException: onChatSent")
            ChatUtil.printErrorChatMessage(ex.toString())
        }
    }
}
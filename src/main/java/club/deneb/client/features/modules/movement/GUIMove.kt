package club.deneb.client.features.modules.movement

import club.deneb.client.features.Category
import club.deneb.client.features.Module
import net.minecraft.client.gui.GuiChat
import net.minecraftforge.client.event.InputUpdateEvent
import org.lwjgl.input.Keyboard

@Module.Info(name = "GUIMove", category = Category.MOVEMENT)
class GUIMove : Module() {

    private val pitchSpeed = setting("PitchSpeed", 6, 0, 20)
    private val yawSpeed = setting("YawSpeed", 6, 0, 20)
    private val chat = setting("Chat", false)
    private val sneak = setting("Sneak", false)

    override fun onTick() {
        if (isEnabled && mc.currentScreen != null && mc.currentScreen !is GuiChat || mc.currentScreen is GuiChat && chat.value) {
            if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                for (i in 0 until pitchSpeed.value) {
                    mc.player.rotationPitch--
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                for (i in 0 until pitchSpeed.value) {
                    mc.player.rotationPitch++
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                for (i in 0 until yawSpeed.value) {
                    mc.player.rotationYaw++
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                for (i in 0 until yawSpeed.value) {
                    mc.player.rotationYaw--
                }
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.keyCode)) {
                mc.player.isSprinting = true
            }
            if (mc.player.rotationPitch > 90) mc.player.rotationPitch = 90f
            if (mc.player.rotationPitch < -90) mc.player.rotationPitch = -90f
        }
    }

    override fun onKey(event: InputUpdateEvent) {
        if (isEnabled && mc.currentScreen != null && mc.currentScreen !is GuiChat || mc.currentScreen is GuiChat && chat.value) {
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.keyCode)) {
                event.movementInput.moveForward = speed
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.keyCode)) {
                event.movementInput.moveForward = -speed
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.keyCode)) {
                event.movementInput.moveStrafe = speed
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindRight.keyCode)) {
                event.movementInput.moveStrafe = -speed
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.keyCode)) {
                event.movementInput.jump = true
            }
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.keyCode) && sneak.value) {
                event.movementInput.sneak = true
            }
        }
    }

    private val speed: Float
        get() {
            var x = 1f
            if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.keyCode) && sneak.value) {
                x = 0.30232558139f
            }
            return x
        }
}
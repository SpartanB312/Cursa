package club.deneb.client.gui

import club.deneb.client.Deneb
import club.deneb.client.client.GuiManager.blue
import club.deneb.client.client.GuiManager.green
import club.deneb.client.client.GuiManager.isSettingRect
import club.deneb.client.client.GuiManager.isSettingSide
import club.deneb.client.client.GuiManager.red
import club.deneb.client.client.GuiManager.transparency
import club.deneb.client.features.Category
import club.deneb.client.features.HUDModule
import club.deneb.client.features.ModuleManager
import club.deneb.client.gui.component.Component
import club.deneb.client.gui.component.ModuleButton
import club.deneb.client.gui.component.ValueButton
import club.deneb.client.gui.font.CFontRenderer
import club.deneb.client.gui.guis.ClickGuiScreen
import club.deneb.client.gui.guis.HUDEditorScreen
import club.deneb.client.utils.LambdaUtil
import club.deneb.client.utils.Timer
import club.deneb.client.utils.Wrapper
import net.minecraft.client.gui.Gui
import java.awt.Color
import java.util.*
import java.util.function.Predicate
import java.util.stream.Collectors
import kotlin.math.max
import kotlin.math.min

/**
 * Created by B_312 on 01/10/21
 */
class Panel(var category: Category, var x: Int, var y: Int, var width: Int, var height: Int) {

    var extended = true
    private var dragging = false
    var isHUD = category.isHUD
    private var x2 = 0
    private var y2 = 0
    var font: CFontRenderer = Deneb.getINSTANCE().font

    var elements: MutableList<ModuleButton> = ArrayList()

    private fun setup() {
        for (m in ModuleManager.getAllModules()) {
            if (m.category === category) {
                elements.add(ModuleButton(m, width, height, this))
            }
        }
    }

    private var panelTimer = Timer()
    fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        if (dragging) {
            x = x2 + mouseX
            y = y2 + mouseY
        }
        val panelColor = -0x7b000000
        val color = Color(red, green, blue, transparency).rgb
        Gui.drawRect(x, y, x + width, y + height, color)
        font.drawString(
            category.categoryName,
            x + (width / 2f - font.getStringWidth(category.categoryName) / 2f),
            y + height / 2f - font.height / 2f + 2,
            -0x101011
        )
        Gui.drawRect(x, y + height, x + width, y + height + 1, panelColor)
        if (elements.isNotEmpty()) {
            var startY = y + height + 2
            var index = 0
            for (button in elements) {
                index++
                //Animation
                if (extended) {
                    if (!panelTimer.passed((index * 25).toDouble())) continue
                } else {
                    if (panelTimer.passed(((elements.size - index) * 25).toDouble())) continue
                }
                button.solvePos()
                button.y = startY
                button.render(mouseX, mouseY, partialTicks)
                if (LambdaUtil.isHovered(mouseX, mouseY).test(button)) {
                    ClickGuiScreen.description = button.module.description
                }
                val settingY = startY - 1
                startY += height + 1
                var extendedCount = 0
                var settingIndex = -1
                val visibleSettings: MutableList<Component> =
                    button.settings.stream().filter { obj: ValueButton<*> -> obj.visible() }
                        .collect(Collectors.toList())
                visibleSettings.add(button.bindButton)
                for (component in visibleSettings) {
                    settingIndex++
                    //Animation
                    if (button.isExtended) {
                        if (!button.buttonTimer.passed((settingIndex * 25).toDouble())) continue
                    } else {
                        if (button.buttonTimer.passed(((visibleSettings.size - settingIndex) * 25).toDouble())) continue
                    }
                    extendedCount++
                    component.solvePos()
                    component.y = startY
                    component.render(mouseX, mouseY, partialTicks)
                    if (component is ValueButton<*>) {
                        if (LambdaUtil.isHovered(mouseX, mouseY).test(component)) {
                            ClickGuiScreen.description = component.setting.description
                        }
                    }
                    startY += height
                }
                if (extendedCount != 0) {
                    if (isSettingRect || isSettingSide) Gui.drawRect(x, settingY, x + 1, startY, color)
                    if (isSettingRect) {
                        Gui.drawRect(x + width, settingY, x + width - 1, startY, color)
                        Gui.drawRect(x + 1, settingY, x + width - 1, settingY + 1, color)
                        Gui.drawRect(x + 1, startY - 1, x + width - 1, startY, color)
                    }
                }
                startY += 1
                if (button.module.isHUD && Wrapper.minecraft.currentScreen is HUDEditorScreen) {
                    val hud = button.module as HUDModule
                    Gui.drawRect(hud.x, hud.y, hud.x + hud.width, hud.y + hud.height, panelColor)
                    hud.onRender()
                }
            }
        }
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        if (mouseButton == 0 && isHovered(mouseX, mouseY).test(this)) {
            x2 = x - mouseX
            y2 = y - mouseY
            dragging = true
            if (!isHUD) {
                Collections.swap(GUIRender.panels, 0, GUIRender.panels.indexOf(this))
            } else {
                Collections.swap(HUDRender.panels, 0, HUDRender.panels.indexOf(this))
            }
            return true
        }
        if (mouseButton == 1 && isHovered(mouseX, mouseY).test(this)) {
            extended = !extended
            panelTimer.reset()
            return true
        }
        return false
    }

    fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        if (state == 0) {
            dragging = false
        }
        for (part in elements) {
            part.mouseReleased(mouseX, mouseY, state)
        }
    }

    fun keyTyped(typedChar: Char, keyCode: Int) {
        for (part in elements) {
            part.keyTyped(typedChar, keyCode)
        }
    }

    private fun isHovered(mouseX: Int, mouseY: Int): Predicate<Panel> {
        return Predicate { c: Panel ->
            mouseX >= min(c.x, c.x + c.width) && mouseX <= max(c.x, c.x + c.width)
                    && mouseY >= min(c.y, c.y + c.height) && mouseY <= max(c.y, c.y + c.height)
        }
    }

    init {
        setup()
    }
}
package club.deneb.client.gui

import club.deneb.client.features.Category
import club.deneb.client.gui.component.ValueButton
import club.deneb.client.features.ModuleManager
import club.deneb.client.features.modules.client.ClickGui
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.util.ArrayList
import java.util.function.Consumer

object GUIRender {

    var panels: ArrayList<Panel> = ArrayList()

    private fun setup() {
        var startX = 5
        for (category in Category.values()) {
            if (category.isHUD || category === Category.HIDDEN) continue
            panels.add(Panel(category, startX, 5, 90, 13))
            startX += 95
        }
    }

    fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        mouseDrag()
        for (i in panels.indices.reversed()) {
            panels[i].drawScreen(mouseX, mouseY, partialTicks)
        }
    }

    fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        for (panel in panels) {
            if (panel.mouseClicked(mouseX, mouseY, mouseButton)) return
            if (!panel.extended) continue
            for (part in panel.elements) {
                if (part.mouseClicked(mouseX, mouseY, mouseButton)) return
                if (!part.isExtended) continue
                for (component in part.settings) {
                    if (component is ValueButton<*>) {
                        if (!component.setting.visible()) continue
                    }
                    if (component.mouseClicked(mouseX, mouseY, mouseButton)) return
                }
                if (part.bindButton.mouseClicked(mouseX, mouseY, mouseButton)) return
            }
        }
    }

    fun keyTyped(typedChar: Char, keyCode: Int) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            ModuleManager.getModule(ClickGui::class.java).disable()
        }
        for (panel in panels) {
            panel.keyTyped(typedChar, keyCode)
        }
    }

    fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        for (panel in panels) {
            panel.mouseReleased(mouseX, mouseY, state)
        }
    }

    fun mouseDrag() {
        val dWheel = Mouse.getDWheel()
        if (dWheel < 0) {
            panels.forEach(Consumer { component: Panel -> component.y -= 10 })
        } else if (dWheel > 0) {
            panels.forEach(Consumer { component: Panel -> component.y += 10 })
        }
    }


    fun getPanelByName(name: String): Panel? {
        var getPane: Panel? = null
        for (panel in panels) {
            if (panel.category.categoryName != name) {
                continue
            }
            getPane = panel
        }
        return getPane
    }

    fun getPanelCategory(c: Category): Panel? {
        var getPane: Panel? = null
        for (panel in panels) {
            if (panel.category != c) {
                continue
            }
            getPane = panel
        }
        return getPane
    }

    init {
        setup()
    }
}
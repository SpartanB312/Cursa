package net.spartanb312.cursa.gui.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.spartanb312.cursa.client.FontManager;
import net.spartanb312.cursa.gui.Component;
import net.spartanb312.cursa.gui.Panel;
import net.spartanb312.cursa.gui.components.ModuleButton;
import net.spartanb312.cursa.client.ModuleManager;
import net.spartanb312.cursa.module.Category;
import net.spartanb312.cursa.module.modules.client.ClickGUI;
import net.spartanb312.cursa.utils.graphics.VertexBuffer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX_COLOR;
import static org.lwjgl.opengl.GL11.*;

public class ClickGUIRenderer {

    public ArrayList<Panel> panels = new ArrayList<>();
    public static ClickGUIRenderer instance = new ClickGUIRenderer();

    public ClickGUIRenderer() {
        int startX = 5;
        for (Category category : Category.values()) {
            if (category == Category.HIDDEN || category.isHUD) continue;
            panels.add(new Panel(category, startX, 5, 105, 15));
            startX += 110;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mouseDrag();

        for (int i = panels.size() - 1; i >= 0; i--) {
            panels.get(i).drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    public Panel getPanelByName(String name) {
        Panel getPane = null;
        if (panels != null)
            for (Panel panel : panels) {
                if (!panel.category.categoryName.equals(name)) {
                    continue;
                }
                getPane = panel;
            }
        return getPane;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Panel panel : panels) {
            if (panel.mouseClicked(mouseX, mouseY, mouseButton)) return;
            if (!panel.extended) continue;
            for (ModuleButton part : panel.elements) {
                if (part.mouseClicked(mouseX, mouseY, mouseButton)) return;
                if (!part.isExtended) continue;
                for (Component component : part.settings) {
                    if (!component.isVisible()) continue;
                    if (component.mouseClicked(mouseX, mouseY, mouseButton)) return;
                }
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            ModuleManager.getModule(ClickGUI.class).disable();
        }
        for (Panel panel : panels) {
            panel.keyTyped(typedChar, keyCode);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Panel panel : panels) {
            panel.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void mouseDrag() {
        int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            panels.forEach(component -> component.y -= 10);
        } else if (dWheel > 0) {
            panels.forEach(component -> component.y += 10);
        }
    }
}

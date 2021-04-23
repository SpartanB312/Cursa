package club.deneb.client.gui;

import club.deneb.client.features.ModuleManager;
import club.deneb.client.features.modules.client.HUDEditor;
import club.deneb.client.gui.component.Component;
import club.deneb.client.gui.component.ModuleButton;
import club.deneb.client.gui.component.ValueButton;
import club.deneb.client.features.Category;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class HUDRender {

    static HUDRender INSTANCE;

    public static HUDRender getINSTANCE() {
        return INSTANCE;
    }

    public ArrayList<Panel> panels = new ArrayList<>();

    public HUDRender() {
        INSTANCE = this;
        setup();
    }

    public void setup() {
        int startX = 5;
        for(Category category : Category.values()){
            if(!category.isHUD()) continue;
            panels.add(new Panel(category, startX, 5, 90, 11));
            startX += 95;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mouseDrag();

        for(int i = panels.size() -1; i>=0 ;i--){
            panels.get(i).drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    public static Panel getPanelByName(String name){
        Panel getPane = null;
        if (INSTANCE.panels != null)
            for (Panel panel : INSTANCE.panels){
                if (!panel.category.getName().equals(name)){
                    continue;
                }
                getPane = panel;
            }
        return getPane;
    }

    public static Panel getPanelCategory(Category c){
        Panel getPane = null;
        if (INSTANCE.panels != null)
            for (Panel panel : INSTANCE.panels){
                if (!panel.category.equals(c)){
                    continue;
                }
                getPane = panel;
            }
        return getPane;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for(Panel panel : panels){
            if(panel.mouseClicked(mouseX, mouseY, mouseButton)) return;
            if (!panel.extended) continue;
            for(ModuleButton part : panel.elements){
                if(part.mouseClicked(mouseX, mouseY, mouseButton)) return;
                if (!part.isExtended) continue;
                for(Component component : part.settings){
                    if (component instanceof ValueButton){
                        if (!((ValueButton<?>) component).getSetting().visible()) continue;
                    }
                    if(component.mouseClicked(mouseX, mouseY, mouseButton)) return;
                }
                if(part.bindButton.mouseClicked(mouseX, mouseY, mouseButton)) return;
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if(keyCode == Keyboard.KEY_ESCAPE){
            ModuleManager.getModule(HUDEditor.class).disable();
        }
        for(Panel panel : panels){
            panel.keyTyped(typedChar, keyCode);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        for(Panel panel : panels){
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

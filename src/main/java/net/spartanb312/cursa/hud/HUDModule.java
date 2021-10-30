package net.spartanb312.cursa.hud;

import net.spartanb312.cursa.core.setting.Setting;
import net.spartanb312.cursa.engine.AsyncRenderer;
import net.spartanb312.cursa.engine.RenderEngine;
import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.utils.graphics.RenderUtils2D;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public abstract class HUDModule extends Module {

    public static final int backgroundColor = new Color(1, 1, 1, 128).getRGB();
    public int x, y, width, height;
    boolean dragging = false;
    int x2, y2;
    protected AsyncRenderer asyncRenderer = null;

    private final Setting<Integer> hud_x = setting("HUD_X", 100, Integer.MIN_VALUE, Integer.MAX_VALUE).when(() -> false);
    private final Setting<Integer> hud_y = setting("HUD_Y", 100, Integer.MIN_VALUE, Integer.MAX_VALUE).when(() -> false);
    private final Setting<Integer> hud_width = setting("HUD_WIDTH", 100, Integer.MIN_VALUE, Integer.MAX_VALUE).when(() -> false);
    private final Setting<Integer> hud_height = setting("HUD_HEIGHT", 100, Integer.MIN_VALUE, Integer.MAX_VALUE).when(() -> false);

    @Override
    public void onSave() {
        hud_x.setValue(x);
        hud_y.setValue(y);
        hud_width.setValue(width);
        hud_height.setValue(height);
        super.onSave();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        x = hud_x.getValue();
        y = hud_y.getValue();
        width = hud_width.getValue();
        height = hud_height.getValue();
    }

    public void onHUDEnable() {
    }

    public void onHUDDisable() {
    }

    @Override
    public final void onEnable() {
        if (asyncRenderer != null) RenderEngine.subscribe(asyncRenderer);
        onHUDEnable();
    }

    @Override
    public final void onDisable() {
        if (asyncRenderer != null) RenderEngine.unsubscribe(asyncRenderer);
        onHUDDisable();
    }

    public HUDModule() {
        this.x = 100;
        this.y = 100;
        this.width = 50;
        this.height = 50;
    }

    public HUDModule(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
    }

    public HUDModule(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public final void renderInHUDEditor(int mouseX, int mouseY, float partialTicks, ScaledResolution resolution) {
        if (dragging) {
            x = x2 + mouseX;
            y = y2 + mouseY;
        }
        RenderUtils2D.drawRect(x, y, x + width, y + height, backgroundColor);
        onHUDRender(resolution);
    }

    public final boolean onMouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0 && isHovered(mouseX, mouseY)) {
            x2 = x - mouseX;
            y2 = y - mouseY;
            dragging = true;
            return true;
        }
        return false;
    }

    public final void onMouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) dragging = false;
    }

    public final boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= Math.min(x, x + width) && mouseX <= Math.max(x, x + width) && mouseY >= Math.min(y, y + height) && mouseY <= Math.max(y, y + height);
    }

    public abstract void onHUDRender(ScaledResolution resolution);

}

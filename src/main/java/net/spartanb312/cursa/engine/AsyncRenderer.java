package net.spartanb312.cursa.engine;

import net.spartanb312.cursa.client.FontManager;
import net.spartanb312.cursa.engine.tasks.RectRenderTask;
import net.spartanb312.cursa.engine.tasks.TextRenderTask;
import net.spartanb312.cursa.utils.graphics.font.CFontRenderer;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AsyncRenderer {

    private final List<RenderTask> renderTasks = new ArrayList<>();
    private final List<RenderTask> tempTasks = new ArrayList<>();
    private final static int white = new Color(255, 255, 255, 255).getRGB();

    public abstract void onUpdate(ScaledResolution resolution, int mouseX, int mouseY);

    public void onUpdate0(ScaledResolution resolution, int mouseX, int mouseY) {
        tempTasks.clear();
        onUpdate(resolution, mouseX, mouseY);
        synchronized (renderTasks) {
            renderTasks.clear();
            renderTasks.addAll(tempTasks);
        }
    }

    public void onRender() {
        List<RenderTask> copiedTasks;
        synchronized (renderTasks) {
            copiedTasks = new ArrayList<>(renderTasks);
        }
        copiedTasks.forEach(RenderTask::onRender);
    }

    public void drawAsyncString(String text, float x, float y) {
        tempTasks.add(new TextRenderTask(text, x, y, white, false, false));
    }

    public void drawAsyncString(String text, float x, float y, int color) {
        tempTasks.add(new TextRenderTask(text, x, y, color, false, false));
    }

    public void drawAsyncCenteredString(String text, float x, float y, int color) {
        tempTasks.add(new TextRenderTask(text, x, y, color, true, false));
    }

    public void drawAsyncStringWithShadow(String text, float x, float y, int color) {
        tempTasks.add(new TextRenderTask(text, x, y, color, false, true));
    }

    public void drawAsyncCenteredStringWithShadow(String text, float x, float y, int color) {
        tempTasks.add(new TextRenderTask(text, x, y, color, true, true));
    }

    public void drawAsyncIcon(float x, float y, int color) {
        drawAsyncString("q", x, y, color, FontManager.iconFont);
    }

    public void drawAsyncString(String text, float x, float y, int color, CFontRenderer fontRenderer) {
        tempTasks.add(new TextRenderTask(text, x, y, color, false, false, fontRenderer));
    }

    public void drawAsyncCenteredString(String text, float x, float y, int color, CFontRenderer fontRenderer) {
        tempTasks.add(new TextRenderTask(text, x, y, color, true, false, fontRenderer));
    }

    public void drawAsyncStringWithShadow(String text, float x, float y, int color, CFontRenderer fontRenderer) {
        tempTasks.add(new TextRenderTask(text, x, y, color, false, true, fontRenderer));
    }

    public void drawAsyncCenteredStringWithShadow(String text, float x, float y, int color, CFontRenderer fontRenderer) {
        tempTasks.add(new TextRenderTask(text, x, y, color, true, true, fontRenderer));
    }

    public void drawAsyncRect(float x, float y, float endX, float endY, int color) {
        tempTasks.add(new RectRenderTask(x, y, endX, endY, color));
    }

    public void drawAsyncRect(float x, float y, float endX, float endY, int color1, int color2, int color3, int color4) {
        tempTasks.add(new RectRenderTask(x, y, endX, endY, color1, color2, color3, color4));
    }

}

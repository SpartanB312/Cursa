package net.spartanb312.cursa.engine.tasks;

import net.spartanb312.cursa.graphics.FontRenderers;
import net.spartanb312.cursa.engine.RenderTask;
import net.spartanb312.cursa.graphics.font.UnicodeFontRenderer;

import java.awt.*;

public class TextRenderTask implements RenderTask {

    String text;
    float x, y;
    Color color;
    boolean centered, shadow;
    UnicodeFontRenderer fontRenderer;
    float scale = 1f;

    public TextRenderTask(String text, float x, float y, int color, boolean centered, boolean shadow) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = new Color(color);
        this.centered = centered;
        this.shadow = shadow;
        this.fontRenderer = FontRenderers.MainFontRenderer;
    }

    public TextRenderTask(String text, float x, float y, int color, boolean centered, boolean shadow, UnicodeFontRenderer fontRenderer) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = new Color(color);
        this.centered = centered;
        this.shadow = shadow;
        this.fontRenderer = fontRenderer;
    }

    public TextRenderTask(String text, float x, float y, int color, boolean centered, boolean shadow, float scale) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = new Color(color);
        this.centered = centered;
        this.shadow = shadow;
        this.scale = scale;
        this.fontRenderer = FontRenderers.MainFontRenderer;
    }

    public TextRenderTask(String text, float x, float y, int color, boolean centered, boolean shadow, float scale, UnicodeFontRenderer fontRenderer) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.color = new Color(color);
        this.centered = centered;
        this.shadow = shadow;
        this.scale = scale;
        this.fontRenderer = fontRenderer;
    }

    @Override
    public void onRender() {
        if (shadow) {
            if (centered) {
                fontRenderer.drawStringWithShadow(text, x - fontRenderer.getWidth(text, scale) / 2f, y - fontRenderer.getHeight(scale) / 2f, 1f, color, scale);
            } else fontRenderer.drawStringWithShadow(text, x, y, color, scale);
        } else {
            if (centered) {
                fontRenderer.drawString(text, x - fontRenderer.getWidth(text, scale) / 2f, y - fontRenderer.getHeight(scale) / 2f, color, scale);
            } else fontRenderer.drawString(text, x, y, color, scale);
        }
    }

}

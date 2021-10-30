package net.spartanb312.cursa.engine.tasks;

import net.spartanb312.cursa.engine.RenderTask;
import net.spartanb312.cursa.utils.graphics.RenderUtils2D;

public class RectRenderTask implements RenderTask {

    float x, y, endX, endY;
    int color1, color2, color3, color4;

    public RectRenderTask(float x, float y, float endX, float endY, int color1, int color2, int color3, int color4) {
        this.x = x;
        this.y = y;
        this.endX = endX;
        this.endY = endY;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.color4 = color4;
    }

    public RectRenderTask(float x, float y, float endX, float endY, int color) {
        this.x = x;
        this.y = y;
        this.endX = endX;
        this.endY = endY;
        this.color1 = color;
        this.color2 = color;
        this.color3 = color;
        this.color4 = color;
    }

    @Override
    public void onRender() {
        RenderUtils2D.drawCustomRect(x, y, endX, endY, color1, color2, color3, color4);
    }

}

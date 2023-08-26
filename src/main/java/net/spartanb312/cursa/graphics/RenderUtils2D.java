package net.spartanb312.cursa.graphics;

import net.minecraft.client.renderer.GlStateManager;

import static org.lwjgl.opengl.GL11.*;

/**
 * Author B_312
 * last update on Sep 12th 2021
 */
public class RenderUtils2D {

    public static void prepareGl() {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.shadeModel(GL_SMOOTH);
        GlStateManager.disableCull();
    }

    public static void releaseGl() {
        GlStateManager.enableTexture2D();
        GlStateManager.enableCull();
    }

    public static void drawRectOutline(float x, float y, float endX, float endY, int color) {
        drawCustomRectOutline(x, y, endX, endY, 1.0F, color, color, color, color);
    }

    public static void drawRectOutline(float x, float y, float endX, float endY, float lineWidth, int color) {
        drawCustomRectOutline(x, y, endX, endY, lineWidth, color, color, color, color);
    }

    public static void drawCustomRectOutline(float x, float y, float endX, float endY, int rightTop, int leftTop, int leftDown, int rightDown) {
        drawCustomRectOutline(x, y, endX, endY, 1.0F, rightTop, leftTop, leftDown, rightDown);
    }

    public static void drawCustomRectOutline(float x, float y, float endX, float endY, float lineWidth, int rightTop, int leftTop, int leftDown, int rightDown) {
        drawCustomLine(endX, y, x, y, lineWidth, rightTop, leftTop); //RightTop -> LeftTop
        drawCustomLine(x, y, x, endY, lineWidth, leftTop, leftDown); //LeftTop -> LeftDown
        drawCustomLine(x, endY, endX, endY, lineWidth, leftDown, rightDown); //LeftDown -> RightDown
        drawCustomLine(endX, endY, endX, y, lineWidth, rightDown, rightTop); //RightDown -> RightTop
    }

    public static void drawRect(float x, float y, float endX, float endY, int color) {
        drawCustomRect(x, y, endX, endY, color, color, color, color);
    }

    public static void drawCustomRect(float x, float y, float endX, float endY, int rightTop, int leftTop, int leftDown, int rightDown) {
        prepareGl();

        VertexBuffer.begin(GL_QUADS);
        VertexBuffer.put(endX, y, rightTop);
        VertexBuffer.put(x, y, leftTop);
        VertexBuffer.put(x, endY, leftDown);
        VertexBuffer.put(endX, endY, rightDown);
        VertexBuffer.end();

        releaseGl();
    }

    public static void drawLine(float startX, float startY, float endX, float endY, int color) {
        drawCustomLine(startX, startY, endX, endY, 1.0F, color, color);
    }

    public static void drawLine(float startX, float startY, float endX, float endY, float lineWidth, int color) {
        drawCustomLine(startX, startY, endX, endY, lineWidth, color, color);
    }

    public static void drawCustomLine(float startX, float startY, float endX, float endY, int startColor, int endColor) {
        drawCustomLine(startX, startY, endX, endY, 1.0F, startColor, endColor);
    }

    public static void drawCustomLine(float startX, float startY, float endX, float endY, float lineWidth, int startColor, int endColor) {
        prepareGl();

        glLineWidth(lineWidth);

        VertexBuffer.begin(GL_LINES);
        VertexBuffer.put(startX, startY, startColor);
        VertexBuffer.put(endX, endY, endColor);
        VertexBuffer.end();

        glLineWidth(1F);

        releaseGl();
    }

}

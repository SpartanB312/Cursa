package net.spartanb312.cursa.graphics;

import net.minecraft.client.renderer.vertex.VertexFormat;
import net.spartanb312.cursa.utils.ColorUtil;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import java.awt.*;
import java.util.function.Supplier;

/**
 * Author B_312
 * last update on Sep 12th 2021
 */
public class VertexBuffer {

    public interface U {
        void invoke(VertexBuffer buffer);
    }

    private static final Tessellator tessellator = Tessellator.getInstance();
    private static final BufferBuilder bufferbuilder = tessellator.getBuffer();

    public static void begin(int mode) {
        bufferbuilder.begin(mode, DefaultVertexFormats.POSITION_COLOR);
    }

    public static void begin(int mode, VertexFormat format) {
        bufferbuilder.begin(mode, format);
    }

    public static void end() {
        tessellator.draw();
    }

    public static void put(double x, double y, int color) {
        bufferbuilder.pos(x, y, 0.0).color(ColorUtil.getRed(color), ColorUtil.getGreen(color), ColorUtil.getBlue(color), ColorUtil.getAlpha(color)).endVertex();
    }

    public static void put(float x, float y, int color) {
        bufferbuilder.pos(x, y, 0.0).color(ColorUtil.getRed(color), ColorUtil.getGreen(color), ColorUtil.getBlue(color), ColorUtil.getAlpha(color)).endVertex();
    }

    public static void put(int x, int y, int color) {
        bufferbuilder.pos(x, y, 0.0).color(ColorUtil.getRed(color), ColorUtil.getGreen(color), ColorUtil.getBlue(color), ColorUtil.getAlpha(color)).endVertex();
    }

    public static void put(double x, double y, Color color) {
        bufferbuilder.pos(x, y, 0.0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    public static void put(float x, float y, Color color) {
        bufferbuilder.pos(x, y, 0.0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    public static void put(int x, int y, Color color) {
        bufferbuilder.pos(x, y, 0.0).color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()).endVertex();
    }

    public static void put(double x, double y, float red, float green, float blue, float alpha) {
        bufferbuilder.pos(x, y, 0.0).color(red, green, blue, alpha).endVertex();
    }

    public static void put(float x, float y, float red, float green, float blue, float alpha) {
        bufferbuilder.pos(x, y, 0.0).color(red, green, blue, alpha).endVertex();
    }

    public static void put(int x, int y, float red, float green, float blue, float alpha) {
        bufferbuilder.pos(x, y, 0.0).color(red, green, blue, alpha).endVertex();
    }

    public static void put(double x, double y, float red, float green, float blue) {
        put(x, y, red, green, blue, 1.0F);
    }

    public static void put(float x, float y, float red, float green, float blue) {
        put(x, y, red, green, blue, 1.0F);
    }

    public static void put(int x, int y, float red, float green, float blue) {
        put(x, y, red, green, blue, 1.0F);
    }

    public static void put(double x, double y, int red, int green, int blue, int alpha) {
        put(x, y, red / 255F, green / 255F, blue / 255F, alpha / 255F);
    }

    public static void put(float x, float y, int red, int green, int blue, int alpha) {
        put(x, y, red / 255F, green / 255F, blue / 255F, alpha / 255F);
    }

    public static void put(int x, int y, int red, int green, int blue, int alpha) {
        put(x, y, red / 255F, green / 255F, blue / 255F, alpha / 255F);
    }

    public static void put(double x, double y, int red, int green, int blue) {
        put(x, y, red / 255F, green / 255F, blue / 255F, 1.0F);
    }

    public static void put(float x, float y, int red, int green, int blue) {
        put(x, y, red / 255F, green / 255F, blue / 255F, 1.0F);
    }

    public static void put(int x, int y, int red, int green, int blue) {
        put(x, y, red / 255F, green / 255F, blue / 255F, 1.0F);
    }

    public static void tex2D(float x, float y, float u, float v, Color color) {
        bufferbuilder.pos(x, y, 0.0)
                .tex(u, v)
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                .endVertex();
    }

}

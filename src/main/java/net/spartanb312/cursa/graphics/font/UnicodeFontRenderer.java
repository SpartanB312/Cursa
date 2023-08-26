package net.spartanb312.cursa.graphics.font;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.spartanb312.cursa.graphics.VertexBuffer;
import net.spartanb312.cursa.graphics.texture.MipmapTexture;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static java.awt.RenderingHints.*;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.POSITION_TEX_COLOR;
import static org.lwjgl.opengl.GL11.*;

/**
 * Author B_312
 * Created on 09/16/2022
 * Updated on 08/27/2023
 */
public class UnicodeFontRenderer implements IFontRenderer, ICenteredFontRenderer, IGradiantFontRenderer {

    private final Font font;
    private final boolean antiAlias;
    private final boolean fractionalMetrics;
    private final int imgSize;
    private final int chunkSize;
    private final boolean linearMag;

    private final CharData[] charDataArray;
    private final float scaledOffset;

    private final MipmapTexture[] textures;
    private final int[] badChunks;

    private int height = 0;
    private float scaleFactor;

    public static final Color shadowColor = new Color(0, 0, 0, 96);

    public UnicodeFontRenderer(Font font, int size, boolean antiAlias, boolean fractionalMetrics, int imgSize, int chunkSize, boolean linearMag, float scaleFactor) {
        this.font = font;
        this.antiAlias = antiAlias;
        this.fractionalMetrics = fractionalMetrics;
        this.imgSize = imgSize;
        this.chunkSize = chunkSize;
        this.linearMag = linearMag;
        charDataArray = new CharData[65536];
        scaledOffset = (4 * size / 25f);
        textures = new MipmapTexture[65536 / chunkSize];
        badChunks = new int[65536 / chunkSize];
        for (int index = 0; index < (65536 / chunkSize); index++) {
            badChunks[index] = 0;
        }
        this.scaleFactor = scaleFactor;

        for (int chunk = 0; chunk < (256 / chunkSize); chunk++) {
            initChunk(chunk);
        }
    }

    public static UnicodeFontRenderer create(String path, float size) {
        return create(path, size, 512, 64, 1f);
    }

    public static UnicodeFontRenderer create(String path, float size, int imgSize, int chunkSize, float scaleFactor) {
        Font font;
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(FontRenderer.class.getResourceAsStream(path)));
        } catch (FontFormatException | IOException e) {
            return null;
        }
        return new UnicodeFontRenderer(font.deriveFont(size).deriveFont(Font.PLAIN), (int) size, true, false, imgSize, chunkSize, false, scaleFactor);
    }

    public UnicodeFontRenderer setScale(float scale) {
        this.scaleFactor = scale;
        return this;
    }

    private MipmapTexture initChunk(int chunk) {
        BufferedImage img = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = (Graphics2D) img.getGraphics();
        graphics.setFont(this.font);
        graphics.setColor(new Color(255, 255, 255, 0));
        graphics.fillRect(0, 0, imgSize, imgSize);
        graphics.setColor(Color.WHITE);

        graphics.setRenderingHint(KEY_FRACTIONALMETRICS, fractionalMetrics ? VALUE_FRACTIONALMETRICS_ON : VALUE_FRACTIONALMETRICS_OFF);
        graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, antiAlias ? VALUE_TEXT_ANTIALIAS_ON : VALUE_TEXT_ANTIALIAS_OFF);
        graphics.setRenderingHint(KEY_ANTIALIASING, antiAlias ? VALUE_ANTIALIAS_ON : VALUE_ANTIALIAS_OFF);

        FontMetrics metrics = graphics.getFontMetrics();
        int charHeight = 0;
        int posX = 0;
        int posY = 1;

        for (int index = 0; index < chunkSize; index++) {
            Rectangle2D dimension = metrics.getStringBounds(String.valueOf((char) (chunk * chunkSize + index)), graphics);
            CharData charData = new CharData(dimension.getBounds().width, dimension.getBounds().height);
            float imgWidth = charData.width + scaledOffset * 2;
            if (charData.height > charHeight) {
                charHeight = charData.height;
                if (charHeight > height) height = charHeight; // Set the max height as Font height
            }
            if (posX + imgWidth > imgSize) {
                posX = 0;
                posY += charHeight;
                charHeight = 0;
            }
            charData.u = (posX + scaledOffset) / (float) imgSize;
            charData.v = posY / (float) imgSize;
            charData.u1 = (posX + scaledOffset + charData.width) / (float) imgSize;
            charData.v1 = (posY + charData.height) / (float) imgSize;
            charDataArray[chunk * chunkSize + index] = charData;
            graphics.drawString(String.valueOf((char) (chunk * chunkSize + index)), posX + scaledOffset, posY + metrics.getAscent());
            posX += imgWidth;
        }

        MipmapTexture texture = new MipmapTexture(img, GL_RGBA, 3);
        texture.bindTexture();
        if (!linearMag) glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        texture.unbindTexture();
        textures[chunk] = texture;
        return texture;
    }

    @Override
    public float getHeight() {
        return height * scaleFactor;
    }

    @Override
    public float getHeight(float scale) {
        return height * scale * scaleFactor;
    }

    @Override
    public float getWidth(String text) {
        return getWidth0(text) * scaleFactor;
    }

    @Override
    public float getWidth(String text, float scale) {
        return getWidth0(text) * scale * scaleFactor;
    }

    private final char[] colorCode = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'r'};

    private int getWidth0(String text) {
        int sum = 0;
        boolean shouldSkip = false;
        for (int index = 0; index < text.length(); index++) {
            if (shouldSkip) {
                shouldSkip = false;
                continue;
            }
            char c = text.charAt(index);
            int chunk = (int) c / chunkSize;
            if (badChunks[chunk] == 1) continue;
            if (textures.length <= chunk) continue;
            if (textures[chunk] == null) {
                MipmapTexture newTexture;
                try {
                    newTexture = initChunk(chunk);
                    textures[chunk] = newTexture;
                } catch (Exception ignored) {
                    badChunks[chunk] = 1;
                    continue;
                }
            }
            int delta = 0;
            CharData data = charDataArray[c];
            if (data != null) delta = data.width;
            if (c == '§' || c == '&') {
                if (index + 1 < text.length()) {
                    char next = text.charAt(index + 1);
                    for (char c1 : colorCode) {
                        if (next == c1) {
                            shouldSkip = true;
                            break;
                        }
                    }
                }
            } else sum += delta;
        }
        return sum;
    }

    @Override
    public void drawString(String text, float x, float y) {
        drawString0(text, x, y, Color.WHITE, 1f, false);
    }

    @Override
    public void drawString(String text, float x, float y, int color) {
        drawString0(text, x, y, new Color(color), 1f, false);
    }

    @Override
    public void drawString(String text, float x, float y, Color color) {
        drawString0(text, x, y, color, 1f, false);
    }

    @Override
    public void drawString(String text, float x, float y, Color color, float scale) {
        drawString0(text, x, y, color, scale, false);
    }

    @Override
    public void drawStringWithShadow(String text, float x, float y) {
        drawString0(text, x + 1f, y + 1f, Color.WHITE, 1f, true);
        drawString0(text, x, y, Color.WHITE, 1f, false);
    }

    @Override
    public void drawStringWithShadow(String text, float x, float y, Color color) {
        drawString0(text, x + 1f, y + 1f, color, 1f, true);
        drawString0(text, x, y, color, 1f, false);
    }

    @Override
    public void drawStringWithShadow(String text, float x, float y, Color color, float scale) {
        drawString0(text, x + 1f, y + 1f, color, scale, true);
        drawString0(text, x, y, color, scale, false);
    }

    @Override
    public void drawStringWithShadow(String text, float x, float y, float shadowDepth) {
        drawString0(text, x + shadowDepth, y + shadowDepth, Color.WHITE, 1f, true);
        drawString0(text, x, y, Color.WHITE, 1f, false);
    }

    @Override
    public void drawStringWithShadow(String text, float x, float y, float shadowDepth, Color color) {
        drawString0(text, x + shadowDepth, y + shadowDepth, color, 1f, true);
        drawString0(text, x, y, color, 1f, false);
    }

    @Override
    public void drawStringWithShadow(String text, float x, float y, float shadowDepth, Color color, float scale) {
        drawString0(text, x + shadowDepth, y + shadowDepth, color, scale, true);
        drawString0(text, x, y, color, scale, false);
    }

    @Override//Centered
    public void drawCenteredString(String text, float x, float y) {
        float width = getWidth(text, 1f);
        float height = this.height * scaleFactor;
        float x1 = x - width / 2f;
        float y1 = y - height / 2f;
        drawString0(text, x1, y1, Color.WHITE, 1f, false);
    }

    @Override
    public void drawCenteredString(String text, float x, float y, int color) {
        float width = getWidth(text, 1f);
        float height = this.height * scaleFactor;
        float x1 = x - width / 2f;
        float y1 = y - height / 2f;
        drawString0(text, x1, y1, new Color(color), 1f, false);
    }

    @Override
    public void drawCenteredString(String text, float x, float y, Color color) {
        float width = getWidth(text, 1f);
        float height = this.height * scaleFactor;
        float x1 = x - width / 2f;
        float y1 = y - height / 2f;
        drawString0(text, x1, y1, color, 1f, false);
    }

    @Override
    public void drawCenteredString(String text, float x, float y, Color color, float scale) {
        float width = getWidth(text, scale);
        float height = this.height * scale * scaleFactor;
        float x1 = x - width / 2f;
        float y1 = y - height / 2f;
        drawString0(text, x1, y1, color, scale, false);
    }

    @Override
    public void drawCenteredStringWithShadow(String text, float x, float y) {
        float width = getWidth(text, 1f);
        float height = this.height * scaleFactor;
        float x1 = x - width / 2f;
        float y1 = y - height / 2f;
        drawString0(text, x1 + 1f, y1 + 1f, Color.WHITE, 1f, true);
        drawString0(text, x1, y1, Color.WHITE, 1f, false);
    }

    @Override
    public void drawCenteredStringWithShadow(String text, float x, float y, Color color) {
        float width = getWidth(text, 1f);
        float height = this.height * scaleFactor;
        float x1 = x - width / 2f;
        float y1 = y - height / 2f;
        drawString0(text, x1 + 1f, y1 + 1f, color, 1f, true);
        drawString0(text, x1, y1, color, 1f, false);
    }

    @Override
    public void drawCenteredStringWithShadow(String text, float x, float y, Color color, float scale) {
        float width = getWidth(text, scale);
        float height = this.height * scale * scaleFactor;
        float x1 = x - width / 2f;
        float y1 = y - height / 2f;
        drawString0(text, x1 + 1f, y1 + 1f, color, scale, true);
        drawString0(text, x1, y1, color, scale, false);
    }

    @Override
    public void drawCenteredStringWithShadow(String text, float x, float y, float shadowDepth) {
        float width = getWidth(text, 1f);
        float height = this.height * scaleFactor;
        float x1 = x - width / 2f;
        float y1 = y - height / 2f;
        drawString0(text, x1 + shadowDepth, y1 + shadowDepth, Color.WHITE, 1f, true);
        drawString0(text, x1, y1, Color.WHITE, 1f, false);
    }

    @Override
    public void drawCenteredStringWithShadow(String text, float x, float y, float shadowDepth, Color color) {
        float width = getWidth(text, 1f);
        float height = this.height * scaleFactor;
        float x1 = x - width / 2f;
        float y1 = y - height / 2f;
        drawString0(text, x1 + shadowDepth, y1 + shadowDepth, color, 1f, true);
        drawString0(text, x1, y1, color, 1f, false);
    }

    @Override
    public void drawCenteredStringWithShadow(String text, float x, float y, float shadowDepth, Color color, float scale) {
        float width = getWidth(text, scale);
        float height = this.height * scale * scaleFactor;
        float x1 = x - width / 2f;
        float y1 = y - height / 2f;
        drawString0(text, x1 + shadowDepth, y1 + shadowDepth, color, scale, true);
        drawString0(text, x1, y1, color, scale, false);
    }

    //Gradiant
    @Override
    public void drawGradiantString(String text, float x, float y, Color[] colors) {
        drawString1(text, x, y, colors, 1f);
    }

    @Override
    public void drawGradiantString(String text, float x, float y, Color[] colors, float scale) {
        drawString1(text, x, y, colors, scale);
    }

    @Override
    public void drawGradiantStringWithShadow(String text, float x, float y, Color[] colors) {
        drawString0(text, x + 1f, y + 1f, colors[0], 1f, true);
        drawString1(text, x, y, colors, 1f);
    }

    @Override
    public void drawGradiantStringWithShadow(String text, float x, float y, Color[] colors, float scale) {
        drawString0(text, x + 1f, y + 1f, colors[0], scale, true);
        drawString1(text, x, y, colors, scale);
    }

    @Override
    public void drawGradiantStringWithShadow(String text, float x, float y, float shadowDepth, Color[] colors) {
        drawString0(text, x + shadowDepth, y + shadowDepth, colors[0], 1f, true);
        drawString1(text, x, y, colors, 1f);
    }

    @Override
    public void drawGradiantStringWithShadow(String text, float x, float y, float shadowDepth, Color[] colors, float scale) {
        drawString0(text, x + shadowDepth, y + shadowDepth, colors[0], scale, true);
        drawString1(text, x, y, colors, scale);
    }

    private void drawString0(String text, float x, float y, Color color0, float scale0, boolean shadow) {
        drawString0(text, x, y, color0, false, new Color[0], scale0, shadow);
    }

    private void drawString1(String text, float x, float y, Color[] colors, float scale0) {
        drawString0(text, x, y, colors[0], true, colors, scale0, false);
    }

    private void drawString0(String text, float x, float y, Color color0, boolean gradiant, Color[] colors, float scale0, boolean shadow) {
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(GL_SMOOTH);
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        float startX = x;
        float startY = y;

        // Color
        int alpha = color0.getAlpha();
        Color currentColor = color0;

        // Scale
        float scale = scale0 * this.scaleFactor;
        if (scale != 1f) {
            glMatrixMode(GL_MODELVIEW);
            glPushMatrix();
            glTranslatef(x, y, 0f);
            glScalef(scale, scale, 1f);
            startX = 0f;
            startY = 0f;
        }

        int chunk = -1;
        boolean shouldSkip = false;

        for (int index = 0; index < text.length(); index++) {
            if (shouldSkip) {
                shouldSkip = false;
                continue;
            }
            char c = text.charAt(index);
            if (c == '\n') {
                startY += height;
                startX = x;
                continue;
            }
            if (c == '§' || c == '&') {
                if (index + 1 < text.length()) {
                    char next = text.charAt(index + 1);
                    //Color
                    Color newColor = getColor(next, color0);
                    if (newColor != null) {
                        if (!gradiant) currentColor = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), alpha);
                        shouldSkip = true;
                        continue;
                    }
                }
            }

            int currentChunk = c / chunkSize;
            if (currentChunk != chunk) {
                chunk = currentChunk;
                MipmapTexture texture = textures[chunk];
                if (texture == null) {
                    // If this is a bad chunk then we skip it
                    if (badChunks[chunk] == 1) continue;
                    MipmapTexture newTexture = null;
                    try {
                        newTexture = initChunk(chunk);
                    } catch (Exception ignore) {
                        badChunks[chunk] = 1;
                    }
                    if (newTexture == null) {
                        continue;
                    } else {
                        textures[chunk] = newTexture;
                        newTexture.bindTexture();
                    }
                } else texture.bindTexture();
            }

            CharData data;
            if (c >= charDataArray.length || charDataArray[c] == null) continue;
            else data = charDataArray[c];

            Color leftColor = shadow ? shadowColor : currentColor;
            Color rightColor = leftColor;
            if (gradiant && !shadow) rightColor = colors[index % colors.length];

            float endX = startX + data.width;
            float endY = startY + data.height;

            VertexBuffer.begin(GL_QUADS, POSITION_TEX_COLOR);
            //RT
            VertexBuffer.tex2D(endX, startY, data.u1, data.v, rightColor);
            //LT
            VertexBuffer.tex2D(startX, startY, data.u, data.v, leftColor);
            //LB
            VertexBuffer.tex2D(startX, endY, data.u, data.v1, leftColor);
            //RB
            VertexBuffer.tex2D(endX, endY, data.u1, data.v1, rightColor);
            VertexBuffer.end();

            startX = endX;
            currentColor = rightColor;
        }
        GlStateManager.bindTexture(0);
        if (scale != 1f) {
            glMatrixMode(GL_MODELVIEW);
            glPopMatrix();
        }
    }

    private Color getColor(char colorCode, Color prev) {
        switch (colorCode) {
            case '0':
                return new Color(0, 0, 0);
            case '1':
                return new Color(0, 0, 170);
            case '2':
                return new Color(0, 170, 0);
            case '3':
                return new Color(0, 170, 170);
            case '4':
                return new Color(170, 0, 0);
            case '5':
                return new Color(170, 0, 170);
            case '6':
                return new Color(255, 170, 0);
            case '7':
                return new Color(170, 170, 170);
            case '8':
                return new Color(85, 85, 85);
            case '9':
                return new Color(85, 85, 255);
            case 'a':
                return new Color(85, 255, 85);
            case 'b':
                return new Color(85, 255, 255);
            case 'c':
                return new Color(255, 85, 85);
            case 'd':
                return new Color(255, 85, 255);
            case 'e':
                return new Color(255, 255, 85);
            case 'f':
                return new Color(255, 255, 255);
            case 'r':
                return prev;
            default:
                return null;
        }
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public static class CharData {
        public final int width;
        public final int height;
        public float u = 0f;
        public float v = 0f;
        public float u1 = 0f;
        public float v1 = 0f;

        public CharData(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

}

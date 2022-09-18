package net.spartanb312.cursa.utils.graphics.texture;

import net.minecraft.client.renderer.GlStateManager;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.concurrent.atomic.AtomicInteger;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

/**
 * Crated on 09/16/2022
 */
public class MipmapTexture {

    private final AtomicInteger states = new AtomicInteger(NOT_READY);
    private final int textureID;
    private int width = 0;
    private int height = 0;

    public MipmapTexture() {
        textureID = glGenTextures();
        // upload image later
    }

    public MipmapTexture(BufferedImage bufferedImage, int format) {
        textureID = glGenTextures();
        // upload image immediately
        uploadImage(bufferedImage, format);
    }

    // Lazy upload
    public void uploadImage(BufferedImage bufferedImage, int format) {
        if (states.get() != NOT_READY) throw new IllegalStateException("Uploaded or deleted");
        GlStateManager.bindTexture(textureID);
        width = bufferedImage.getWidth();
        height = bufferedImage.getHeight();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        upload(bufferedImage, format, bufferedImage.getWidth(), bufferedImage.getHeight());
        unbindTexture();
    }

    private void upload(BufferedImage bufferedImage, int format, int width, int height) {
        int[] intArray = new int[width * height];
        bufferedImage.getRGB(0, 0, width, height, intArray, 0, width);
        IntBuffer buffer = ByteBuffer.allocateDirect(4 * width * height).order(ByteOrder.nativeOrder()).asIntBuffer();
        buffer.put(intArray);
        buffer.flip();
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, buffer);
        states.set(READY);
    }

    public int getTextureID() {
        return textureID;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void bindTexture() {
        if (isReady()) GlStateManager.bindTexture(textureID);
        else {
            if (isDeleted()) throw new IllegalStateException("This texture is already deleted!");
            else throw new IllegalStateException("This texture is not ready!");
        }
    }

    public void unbindTexture() {
        GlStateManager.bindTexture(0);
    }

    public void deleteTexture() {
        glDeleteTextures(textureID);
        states.set(DELETED);
    }

    public boolean isReady() {
        return states.get() == READY;
    }

    public boolean isDeleted() {
        return states.get() == DELETED;
    }

    public static int READY = 1;
    public static int NOT_READY = 0;
    public static int DELETED = -1;

}

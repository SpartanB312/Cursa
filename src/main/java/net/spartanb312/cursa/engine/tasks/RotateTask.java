package net.spartanb312.cursa.engine.tasks;

import net.spartanb312.cursa.engine.RenderTask;
import org.lwjgl.opengl.GL11;

public class RotateTask implements RenderTask {

    float angle;
    float x;
    float y;
    float z;
    int matrixMode = NONE;

    public RotateTask(float angle, float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public RotateTask(float angle, float x, float y, float z, int matrixMode) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.matrixMode = matrixMode;
    }

    @Override
    public void onRender() {
        if (matrixMode == PRE) {
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPushMatrix();
        }
        GL11.glRotatef(angle, x, y, z);
        if (matrixMode == POST) {
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPopMatrix();
        }
    }

    public static int PRE = -1;
    public static int NONE = 0;
    public static int POST = 1;

}

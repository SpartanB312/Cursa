package club.deneb.client.utils.particles;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class ParticleSystem {

    private static final float SPEED = 0.1f;
    private final List<Particle> particleList = new ArrayList<>();

    public ParticleSystem(int initAmount) {
        this.addParticles(initAmount);
    }

    public void addParticles(int n) {
        for (int i = 0; i < n; ++i) {
            this.particleList.add(Particle.generateParticle());
        }
    }

    public static double distance(float x, float y, float x1, float y1) {
        return Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
    }

    public void tick(int delta) {
        //if (Mouse.isButtonDown(0)) addParticles(1);
        for (Particle particle : particleList) {
            particle.tick(delta, SPEED);
        }
    }

    private void drawLine(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8) {
        GL11.glColor4f(f5, f6, f7, f8);
        GL11.glLineWidth(0.5f);
        GL11.glBegin(1);
        GL11.glVertex2f(f, f2);
        GL11.glVertex2f(f3, f4);
        GL11.glEnd();
    }

    public void render() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2884);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);

        if (Minecraft.getMinecraft().currentScreen == null) {
            return;
        }

        for (Particle particle : particleList) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, (particle.getAlpha() / 255.0f));
            GL11.glPointSize(particle.getSize());
            GL11.glBegin(0);
            GL11.glVertex2f(particle.getX(), particle.getY());
            GL11.glEnd();

            int Width = Mouse.getEventX() * Minecraft.getMinecraft().currentScreen.width / Minecraft.getMinecraft().displayWidth;
            int Height = Minecraft.getMinecraft().currentScreen.height - Mouse.getEventY() * Minecraft.getMinecraft().currentScreen.height / Minecraft.getMinecraft().displayHeight - 1;

            float nearestDistance = 0.0f;
            Particle nearestParticle = null;
            int dist = 100;

            for (Particle particle1 : this.particleList) {
                float distance = particle.getDistanceTo(particle1);
                if (distance > dist || distance(Width, Height, particle.getX(), particle.getY()) > dist && distance(Width, Height, particle1.getX(), particle1.getY()) > dist || nearestDistance > 0.0f && distance > nearestDistance) {
                    continue;
                }
                nearestDistance = distance;
                nearestParticle  = particle1;
            }

            if (nearestParticle == null) {
                continue;
            }
            float alpha = Math.min(1.0f, Math.min(1.0f, 1.0f - nearestDistance / dist));
            drawLine(particle.getX(), particle.getY(), nearestParticle.getX(), nearestParticle.getY(), 1.0f, 1.0f, 1.0f, alpha);
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(0.5f, 0.5f, 0.5f);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(2884);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
}

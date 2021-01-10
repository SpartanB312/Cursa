package com.deneb.client.event.events;

import com.deneb.client.event.MinecraftEvent;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.math.Vec3d;


/**
 * Created by 086 on 10/12/2017.
 */
public class RenderEvent extends MinecraftEvent {

    private final Tessellator tessellator;
    private final Vec3d renderPos;

    public RenderEvent(Tessellator tessellator, Vec3d renderPos) {
        super();
        this.tessellator = tessellator;
        this.renderPos = renderPos;
    }

    public Tessellator getTessellator() {
        return tessellator;
    }

    public BufferBuilder getBuffer() {
        return tessellator.getBuffer();
    }

    public Vec3d getRenderPos() {
        return renderPos;
    }

    public void setTranslation(Vec3d translation) {
        getBuffer().setTranslation(-translation.x, -translation.y, -translation.z);
    }

    public void resetTranslation() {
        setTranslation(renderPos);
    }

}

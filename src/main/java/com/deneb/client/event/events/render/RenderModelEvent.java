package com.deneb.client.event.events.render;

import com.deneb.client.event.MinecraftEvent;

public class RenderModelEvent extends MinecraftEvent {
    public boolean rotating = false;
    public float pitch = 0;

    public RenderModelEvent(){
        super();
    }
}

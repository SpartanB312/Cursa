package net.spartanb312.cursa.event.events.render;

import net.spartanb312.cursa.event.CursaEvent;

public class RenderModelEvent extends CursaEvent {
    public boolean rotating = false;
    public float pitch = 0;

    public RenderModelEvent(){
        super();
    }
}

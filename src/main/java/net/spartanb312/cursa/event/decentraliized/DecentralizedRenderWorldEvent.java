package net.spartanb312.cursa.event.decentraliized;

import net.spartanb312.cursa.core.event.decentralization.DecentralizedEvent;
import net.spartanb312.cursa.event.events.render.RenderWorldEvent;

public class DecentralizedRenderWorldEvent extends DecentralizedEvent<RenderWorldEvent> {
    public static DecentralizedRenderWorldEvent instance = new DecentralizedRenderWorldEvent();
}

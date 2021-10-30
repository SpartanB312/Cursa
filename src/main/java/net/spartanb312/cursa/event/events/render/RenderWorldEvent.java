package net.spartanb312.cursa.event.events.render;

import net.spartanb312.cursa.core.event.decentralization.EventData;
import net.spartanb312.cursa.event.CursaEvent;

public final class RenderWorldEvent extends CursaEvent implements EventData {

    private final float partialTicks;
    private final Pass pass;

    public RenderWorldEvent(float partialTicks, int pass) {
        this.partialTicks = partialTicks;
        this.pass = Pass.values()[pass];
    }

    public final Pass getPass() {
        return this.pass;
    }

    public final float getPartialTicks() {
        return partialTicks;
    }

    public enum Pass {
        ANAGLYPH_CYAN, ANAGLYPH_RED, NORMAL
    }

}

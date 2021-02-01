package club.deneb.client.event;

import club.deneb.client.utils.Wrapper;
import net.minecraftforge.fml.common.eventhandler.Event;;

public class MinecraftEvent extends Event {
    private boolean cancelled;
    public Era era = Era.PRE;
    private final float partialTicks;

    public MinecraftEvent()
    {
        partialTicks = Wrapper.getMinecraft().getRenderPartialTicks();
    }

    public MinecraftEvent(Era p_Era)
    {
        partialTicks = Wrapper.getMinecraft().getRenderPartialTicks();
        era = p_Era;
    }

    public Era getEra()
    {
        return era;
    }

    public final boolean isCancelled() {
        return this.cancelled;
    }

    public final void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public final void cancel() {
        this.setCancelled(true);
    }

    public float getPartialTicks()
    {
        return partialTicks;
    }

    public void setEra(Era era) {
        this.era = era;
    }

    public enum Era
    {
        PRE,
        PERI,
        POST
    }
}

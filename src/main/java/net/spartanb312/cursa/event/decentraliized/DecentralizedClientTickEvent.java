package net.spartanb312.cursa.event.decentraliized;

import net.spartanb312.cursa.core.event.decentralization.DecentralizedEvent;
import net.spartanb312.cursa.core.event.decentralization.EventData;

public class DecentralizedClientTickEvent extends DecentralizedEvent<EventData> {
    public static DecentralizedClientTickEvent instance = new DecentralizedClientTickEvent();
}

package net.spartanb312.cursa.event.decentraliized;

import net.spartanb312.cursa.core.event.decentralization.DecentralizedEvent;
import net.spartanb312.cursa.event.events.network.PacketEvent;

public class DecentralizedPacketEvent {
    public static class Send extends DecentralizedEvent<PacketEvent.Send> {
        public static Send instance = new Send();
    }

    public static class Receive extends DecentralizedEvent<PacketEvent.Receive> {
        public static Receive instance = new Receive();
    }
}

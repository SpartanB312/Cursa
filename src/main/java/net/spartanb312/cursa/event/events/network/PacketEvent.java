package net.spartanb312.cursa.event.events.network;

import net.spartanb312.cursa.core.event.decentralization.EventData;
import net.spartanb312.cursa.event.CursaEvent;
import net.minecraft.network.Packet;

public class PacketEvent extends CursaEvent implements EventData {

    public final Packet<?> packet;

    public PacketEvent(final Packet<?> packet) {
        this.packet = packet;
    }

    public static class Receive extends PacketEvent {
        public Receive(final Packet<?> packet) {
            super(packet);
        }
    }

    public static class Send extends PacketEvent {
        public Send(final Packet<?> packet) {
            super(packet);
        }
    }

    public Packet<?> getPacket() {
        return packet;
    }
}

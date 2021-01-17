package com.deneb.client.event.events.client;

import com.deneb.client.event.MinecraftEvent;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * Created by B_312 on 01/10/21.
 */
@Cancelable
public class PacketEvent extends MinecraftEvent {

    public Packet packet;

    public PacketEvent(final Packet<?> packet) {
        super();
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

    public Packet getPacket(){
        return packet;
    }
    public boolean isCancelable() {
        return true;
    }
}

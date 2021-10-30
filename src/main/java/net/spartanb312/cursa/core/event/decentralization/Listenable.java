package net.spartanb312.cursa.core.event.decentralization;

import net.spartanb312.cursa.core.concurrent.task.Task;

import java.util.concurrent.ConcurrentHashMap;

public interface Listenable {

    ConcurrentHashMap<DecentralizedEvent<? extends EventData>, Task<? extends EventData>> listenerMap();

    void subscribe();

    void unsubscribe();

}

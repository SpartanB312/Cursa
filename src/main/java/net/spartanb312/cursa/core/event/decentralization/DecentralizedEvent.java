package net.spartanb312.cursa.core.event.decentralization;

import net.spartanb312.cursa.core.concurrent.task.Task;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("ALL")
public class DecentralizedEvent<T extends EventData> {

    private final List<Task<T>> listeners = new CopyOnWriteArrayList<>();

    public void register(Task<? extends EventData> action) {
        if (listeners.stream().anyMatch(it -> it.equals(action))) return;
        listeners.add((Task<T>) action);
    }

    public void unregister(Task<? extends EventData> action)  {
        listeners.removeIf(it -> it.equals(action));
    }

    public void post(T data) {
        listeners.forEach(it -> {
            it.invoke(data);
        });
    }

}

package net.spartanb312.cursa.core.event.decentralization;

import net.spartanb312.cursa.core.concurrent.task.Task;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

public class ListenableImpl implements Listenable {

    private final ConcurrentHashMap<DecentralizedEvent<? extends EventData>, Task<? extends EventData>> listenerMap = new ConcurrentHashMap<>();

    @Override
    public ConcurrentHashMap<DecentralizedEvent<? extends EventData>, Task<? extends EventData>> listenerMap() {
        return listenerMap;
    }

    @Override
    public void subscribe() {
        listenerMap.forEach(DecentralizedEvent::register);
    }

    @Override
    public void unsubscribe() {
        listenerMap.forEach(DecentralizedEvent::unregister);
    }

    public <T extends EventData> void listener(Class<? extends DecentralizedEvent<T>> eventClass, Task<T> action) {
        listener(this, eventClass, action);
    }

    public static <T extends EventData> void listener(Listenable listenable, Class<? extends DecentralizedEvent<T>> eventClass, Task<T> action) {
        try {
            DecentralizedEvent<? extends EventData> instance = tryGetInstance(eventClass);
            if (instance != null) {
                listenable.listenerMap().put(instance, action);
                return;
            }
            throw new NoSuchFieldException("Can't find instant static field in the DecentralizedEvent class : " + eventClass.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T extends EventData> DecentralizedEvent<? extends EventData> tryGetInstance(Class<? extends DecentralizedEvent<T>> eventClass) {
        try {
            for (Field field : eventClass.getDeclaredFields()) {
                if (field.getType().equals(eventClass) && Modifier.isStatic(field.getModifiers())) {
                    return (DecentralizedEvent<? extends EventData>) field.get(null);
                }
            }
        } catch (IllegalAccessException ignore) {
        }
        return null;
    }

}

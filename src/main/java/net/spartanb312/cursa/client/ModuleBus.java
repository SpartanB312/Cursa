package net.spartanb312.cursa.client;

import net.spartanb312.cursa.Cursa;
import net.spartanb312.cursa.core.event.Listener;
import net.spartanb312.cursa.core.event.Priority;
import net.spartanb312.cursa.core.event.decentralization.ListenableImpl;
import net.spartanb312.cursa.event.decentraliized.DecentralizedClientTickEvent;
import net.spartanb312.cursa.event.decentraliized.DecentralizedRenderTickEvent;
import net.spartanb312.cursa.event.decentraliized.DecentralizedRenderWorldEvent;
import net.spartanb312.cursa.event.events.client.InputUpdateEvent;
import net.spartanb312.cursa.event.events.client.SettingUpdateEvent;
import net.spartanb312.cursa.event.events.network.PacketEvent;
import net.spartanb312.cursa.event.events.render.RenderOverlayEvent;
import net.spartanb312.cursa.event.events.render.RenderWorldEvent;
import net.spartanb312.cursa.module.Module;
import net.spartanb312.cursa.notification.NotificationManager;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.spartanb312.cursa.core.concurrent.ConcurrentTaskManager.runParallel;

public class ModuleBus extends ListenableImpl {

    public ModuleBus() {
        Cursa.EVENT_BUS.register(this);
        listener(DecentralizedClientTickEvent.class, it -> onTick());
        listener(DecentralizedRenderTickEvent.class, this::onRenderTick);
        listener(DecentralizedRenderWorldEvent.class, this::onRenderWorld);
        subscribe();
    }

    private final List<Module> modules = new CopyOnWriteArrayList<>();

    public synchronized void register(Module module) {
        modules.add(module);
        Cursa.EVENT_BUS.register(module);
    }

    public void unregister(Module module) {
        modules.remove(module);
        Cursa.EVENT_BUS.unregister(module);
    }

    public boolean isRegistered(Module module) {
        return modules.contains(module);
    }

    public List<Module> getModules() {
        return modules;
    }

    @Listener(priority = Priority.HIGHEST)
    public void onKey(InputUpdateEvent event) {
        modules.forEach(mod -> mod.onInputUpdate(event));
    }

    public void onTick() {
        runParallel(it -> modules.forEach(module -> {
            if (module.parallelRunnable) {
                it.launch(() -> {
                    try {
                        module.onTick();
                    } catch (Exception exception) {
                        NotificationManager.fatal("Error while running Parallel Tick!");
                        exception.printStackTrace();
                    }
                });
            } else {
                try {
                    module.onTick();
                } catch (Exception exception) {
                    NotificationManager.fatal("Error while running Tick!");
                    exception.printStackTrace();
                }
            }
        }));
    }

    @Listener(priority = Priority.HIGHEST)
    public void onRenderTick(RenderOverlayEvent event) {
        runParallel(it -> modules.forEach(module -> {
            try {
                module.onRender(event);
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running onRender!");
                exception.printStackTrace();
            }
            if (module.parallelRunnable) {
                it.launch(() -> {
                    try {
                        module.onRenderTick();
                    } catch (Exception exception) {
                        NotificationManager.fatal("Error while running Parallel Render Tick!");
                        exception.printStackTrace();
                    }
                });
            } else {
                try {
                    module.onRenderTick();
                } catch (Exception exception) {
                    NotificationManager.fatal("Error while running Render Tick!");
                    exception.printStackTrace();
                }
            }
        }));
    }

    @Listener(priority = Priority.HIGHEST)
    public void onRenderWorld(RenderWorldEvent event) {
        WorldRenderPatcher.INSTANCE.patch(event);
    }

    @Listener(priority = Priority.HIGHEST)
    public void onPacketSend(PacketEvent.Send event) {
        modules.forEach(module -> {
            try {
                module.onPacketSend(event);
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running PacketSend!");
                exception.printStackTrace();
            }
        });
    }

    @Listener(priority = Priority.HIGHEST)
    public void onPacketReceive(PacketEvent.Receive event) {
        modules.forEach(module -> {
            try {
                module.onPacketReceive(event);
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running PacketReceive!");
                exception.printStackTrace();
            }
        });
    }

    @Listener(priority = Priority.HIGHEST)
    public void onSettingChange(SettingUpdateEvent event) {
        modules.forEach(it -> {
            try {
                it.onSettingChange(event.getSetting());
            } catch (Exception exception) {
                NotificationManager.fatal("Error while running onSettingChange!");
                exception.printStackTrace();
            }
        });
    }

}

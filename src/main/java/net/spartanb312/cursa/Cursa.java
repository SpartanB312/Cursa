package net.spartanb312.cursa;

import net.spartanb312.cursa.core.event.EventManager;
import net.spartanb312.cursa.core.event.Listener;
import net.spartanb312.cursa.core.event.Priority;
import net.spartanb312.cursa.event.events.client.InitializationEvent;
import net.spartanb312.cursa.module.modules.client.ClickGUI;
import net.spartanb312.cursa.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import static net.spartanb312.cursa.core.concurrent.ConcurrentTaskManager.runBlocking;
import static net.spartanb312.cursa.core.concurrent.ConcurrentTaskManager.runTiming;

/**
 * Author B_312
 * Since 05/01/2021
 * Last update on 09/21/2021
 */
public class Cursa {

    public static final String MOD_NAME = "Cursa";
    public static final String MOD_VERSION = "0.0.1-dev";

    public static final String AUTHOR = "B_312";
    public static final String GITHUB = "https://github.com/SpartanB312/Cursa";

    public static String CHAT_SUFFIX = "\u1d04\u1d1c\u0280\u0073\u1d00";

    public static final Logger log = LogManager.getLogger(MOD_NAME);
    private static Thread mainThread;

    @Listener(priority = Priority.HIGHEST)
    public void preInitialize(InitializationEvent.PreInitialize event) {
        mainThread = Thread.currentThread();
    }

    @Listener(priority = Priority.HIGHEST)
    public void initialize(InitializationEvent.Initialize event) {
        long tookTime = runTiming(() -> {
            Display.setTitle(MOD_NAME + " " + MOD_VERSION);

            //Parallel load managers
            runBlocking(it -> {

                Cursa.log.info("Loading Font Manager");
                FontManager.init();

                Cursa.log.info("Loading Module Manager");
                ModuleManager.init();

                Cursa.log.info("Loading GUI Manager");
                it.launch(GUIManager::init);

                Cursa.log.info("Loading Command Manager");
                it.launch(CommandManager::init);

                Cursa.log.info("Loading Friend Manager");
                it.launch(FriendManager::init);

                Cursa.log.info("Loading Config Manager");
                it.launch(ConfigManager::init);

            });
        });

        log.info("Took " + tookTime + "ms to launch Cursa!");
    }

    @Listener(priority = Priority.HIGHEST)
    public void postInitialize(InitializationEvent.PostInitialize event) {
        ClickGUI.instance.disable();
    }

    public static boolean isMainThread(Thread thread) {
        return thread == mainThread;
    }

    public static EventManager EVENT_BUS = new EventManager();
    public static ModuleBus MODULE_BUS = new ModuleBus();

    public static final Cursa instance = new Cursa();

}
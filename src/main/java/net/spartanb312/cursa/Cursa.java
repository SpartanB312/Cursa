package net.spartanb312.cursa;

import net.spartanb312.cursa.client.*;
import net.spartanb312.cursa.core.event.EventManager;
import net.spartanb312.cursa.core.event.Listener;
import net.spartanb312.cursa.core.event.Priority;
import net.spartanb312.cursa.event.events.client.InitializationEvent;
import net.spartanb312.cursa.graphics.FontRenderers;
import net.spartanb312.cursa.module.modules.client.ClickGUI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

import static net.spartanb312.cursa.core.concurrent.ConcurrentTaskManager.runParallel;
import static net.spartanb312.cursa.core.concurrent.ConcurrentTaskManager.measureTime;

/**
 * Author B_312
 * Since 05/01/2021
 * Last update on 09/16/2022
 */
public class Cursa {

    public static final String MOD_ID = "cursa";
    public static final String MOD_NAME = "Cursa";
    public static final String MOD_VERSION = "0.0.2";
    public static final String MOD_BRANCH = "dev";
    public static final String FULL_MOD_VERSION = MOD_VERSION + "-" + MOD_BRANCH;

    public static final String AUTHOR = "B_312";
    public static final String GITHUB = "https://github.com/SpartanB312/Cursa";

    public static String CHAT_SUFFIX = "\u1d04\u1d1c\u0280\u0073\u1d00";

    public static final Logger log = LogManager.getLogger(MOD_NAME);
    private static Thread mainThread;

    @Listener(priority = Priority.HIGHEST)
    public void preInitialize(InitializationEvent.PreInitialize event) {
        mainThread = Thread.currentThread();
    }

    @SuppressWarnings("deprecation")
    @Listener(priority = Priority.HIGHEST)
    public void initialize(InitializationEvent.Initialize event) {
        long tookTime = measureTime(() -> {
            Display.setTitle(MOD_NAME + " " + FULL_MOD_VERSION);

            //Parallel load managers
            runParallel(it -> {

                Cursa.log.info("Loading font renderers");
                FontRenderers.init();

                Cursa.log.info("Loading modules");
                ModuleManager.init();

                Cursa.log.info("Loading GUI");
                it.launch(GUIManager::init);

                Cursa.log.info("Loading commands");
                it.launch(CommandManager::init);

                Cursa.log.info("Loading friends");
                it.launch(FriendManager::init);

                Cursa.log.info("Loading configs");
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
package net.spartanb312.cursa.core.concurrent.repeat;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static net.spartanb312.cursa.core.concurrent.ConcurrentTaskManager.launch;

/**
 * Created by B_312 on 05/01/2021
 */
public class RepeatManager {

    public static RepeatManager instance;

    public static void init() {
        if (instance == null) instance = new RepeatManager();
    }

    public final List<RepeatJob> repeatJobs = new CopyOnWriteArrayList<>();
    public final List<DelayJob> delayJobs = new CopyOnWriteArrayList<>();

    public static void update() {
        instance.delayJobs.removeIf(DelayJob::invoke);
        instance.repeatJobs.removeIf(RepeatJob::isDead);
        instance.repeatJobs.forEach(it -> {
            if (it.shouldRun()) {
                launch(it::run);
            }
        });
    }

}

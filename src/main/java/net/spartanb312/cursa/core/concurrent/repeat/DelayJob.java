package net.spartanb312.cursa.core.concurrent.repeat;

import net.spartanb312.cursa.core.concurrent.task.VoidTask;

public class DelayJob {

    private final VoidTask task;
    private final long startTime;

    public DelayJob(long startTime, VoidTask task) {
        this.task = task;
        this.startTime = startTime;
    }

    public boolean invoke() {
        if (System.currentTimeMillis() >= startTime) {
            task.invoke();
            return true;
        }
        return false;
    }

}

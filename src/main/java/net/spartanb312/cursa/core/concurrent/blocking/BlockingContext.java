package net.spartanb312.cursa.core.concurrent.blocking;

import net.spartanb312.cursa.core.concurrent.ConcurrentTaskManager;
import net.spartanb312.cursa.core.concurrent.task.VoidTask;
import net.spartanb312.cursa.core.concurrent.utils.Syncer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockingContext {

    private final List<BlockingJob> tasks = new ArrayList<>();
    private final AtomicInteger finished = new AtomicInteger(0);
    private Syncer syncer;

    public void launch(VoidTask task) {
        BlockingJob unit = new BlockingJob(task, this);
        ConcurrentTaskManager.instance.executor.execute(unit);
        tasks.add(unit);
    }

    public synchronized void count() {
        synchronized (finished) {
            finished.incrementAndGet();
        }
    }

    public void await() {
        if (tasks.size() == 0) return;
        synchronized (finished) {
            syncer = new Syncer(tasks.size() - finished.get());
        }
        syncer.await();
    }

    public synchronized void countDown() {
        if (syncer != null) syncer.countDown();
    }

}

package net.spartanb312.cursa.core.concurrent;

import net.spartanb312.cursa.core.concurrent.blocking.BlockingContext;
import net.spartanb312.cursa.core.concurrent.blocking.BlockingTask;
import net.spartanb312.cursa.core.concurrent.repeat.DelayJob;
import net.spartanb312.cursa.core.concurrent.repeat.RepeatManager;
import net.spartanb312.cursa.core.concurrent.repeat.RepeatJob;
import net.spartanb312.cursa.core.concurrent.task.*;
import net.spartanb312.cursa.core.concurrent.thread.BackgroundMainThread;
import net.spartanb312.cursa.core.concurrent.utils.Syncer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.IntSupplier;

/**
 * Created by B_312 on 05/01/2021
 * Meme codes. Lol
 */
public class ConcurrentTaskManager {

    //---- Instance Stuff ----//
    public static ConcurrentTaskManager instance = new ConcurrentTaskManager();

    public static final int workingThreads = Runtime.getRuntime().availableProcessors();

    public BackgroundMainThread backgroundMainThread = new BackgroundMainThread();

    public final ThreadPoolExecutor executor = new ThreadPoolExecutor(workingThreads, Integer.MAX_VALUE, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    ConcurrentTaskManager() {
        RepeatManager.init();
        backgroundMainThread.start();
    }

    public static long measureTime(VoidTask task) {
        long startTime = System.currentTimeMillis();
        task.invoke();
        return System.currentTimeMillis() - startTime;
    }

    //---- TaskPool Runner ----//
    @Deprecated // inefficient synchronized in lightweight tasks, use runParallelTasks instead
    public static void runParallel(BlockingTask task) {
        BlockingContext content = new BlockingContext();
        task.invoke(content);
        content.await();
    }

    public static void runParallelTasks(VoidTask... tasks) {
        runParallelTasks(Arrays.asList(tasks));
    }

    public static void runParallelTasks(List<VoidTask> tasks) {
        Syncer syncer = new Syncer(tasks.size());
        tasks.forEach(it -> instance.executor.execute(new VoidRunnable(it, syncer)));
        syncer.await();
    }

    public static <T> void runParameterParallels(List<MultiParameterTask<T>> tasks, T[] parameters) {
        Syncer syncer = new Syncer(tasks.size());
        tasks.forEach(it -> instance.executor.execute(new TaskRunnable<>(it, syncer, parameters)));
        syncer.await();
    }

    public static void launch(VoidTask task) {
        instance.executor.execute(new VoidRunnable(task));
    }

    public static <T> void launch(Syncer syncer, T parameters, Task<T> task) {
        instance.executor.execute(new TaskRunnable<>(task, syncer, parameters));
    }

    public static <T> void launch(T parameter, Task<T> task) {
        instance.executor.execute(new TaskRunnable<>(parameter, task));
    }

    public static <T> void launch(Syncer syncer, T[] parameters, MultiParameterTask<T> task) {
        instance.executor.execute(new TaskRunnable<>(task, syncer, parameters));
    }

    public static <T> void launch(T[] parameters, MultiParameterTask<T> task) {
        instance.executor.execute(new TaskRunnable<>(parameters, task));
    }

    public static void launch(Syncer syncer, VoidTask task) {
        instance.executor.execute(new VoidRunnable(task, syncer));
    }

    //---- Delay Runner ----//
    public static void addDelayTask(int delay, VoidTask task) {
        RepeatManager.instance.delayJobs.add(new DelayJob(System.currentTimeMillis() + delay, task));
    }

    public static void addDelayTask(DelayJob delayJob) {
        RepeatManager.instance.delayJobs.add(delayJob);
    }

    //---- Repeat Runner ----//
    public static void runRepeat(RepeatJob unit) {
        registerRepeatUnit(unit);
    }

    public static void runRepeat(int delay, VoidTask task) {
        RepeatJob unit = new RepeatJob(delay, task);
        registerRepeatUnit(unit);
    }

    public static void runRepeat(int delay, int times, VoidTask task) {
        RepeatJob unit = new RepeatJob(delay, times, task);
        registerRepeatUnit(unit);
    }

    public static void runRepeat(IntSupplier delay, VoidTask task) {
        RepeatJob unit = new RepeatJob(delay, task);
        registerRepeatUnit(unit);
    }

    public static void runRepeat(IntSupplier delay, int times, VoidTask task) {
        RepeatJob unit = new RepeatJob(delay, times, task);
        registerRepeatUnit(unit);
    }

    public static void registerRepeatUnit(RepeatJob repeatJob) {
        RepeatManager.instance.repeatJobs.add(repeatJob);
    }

    public static void unregisterRepeatUnit(RepeatJob repeatJob) {
        RepeatManager.instance.repeatJobs.remove(repeatJob);
    }

    public static void repeat(int times, VoidTask task) {
        for (int i = 0; i < times; i++) {
            task.invoke();
        }
    }

    public void stop() {
        try {
            executor.shutdown();
        } catch (Exception ignore) {
            System.out.println("[TaskManager]TaskManager shut down!");
        }
    }

    //---- Background Stuff ----//
    public static void updateBackground() {
        RepeatManager.update();
    }

}

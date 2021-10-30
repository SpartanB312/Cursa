package net.spartanb312.cursa.core.concurrent.task;

import net.spartanb312.cursa.core.concurrent.utils.Syncer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by B_312 on 05/01/2021
 */
public class TaskRunnable<T> extends Syncable {

    private final MultiParameterTask<T> multiParameterTask;
    private final List<T> parameters = new ArrayList<>();

    private final Task<T> task;
    private T eventParameter = null;

    public TaskRunnable(Task<T> task) {
        this.task = task;
        this.multiParameterTask = null;
        this.syncer = null;
    }

    public TaskRunnable(Task<T> task, Syncer syncer) {
        this.task = task;
        this.syncer = syncer;
        this.multiParameterTask = null;
        this.syncer = null;
    }

    public TaskRunnable(T eventParameter, Task<T> task) {
        this.task = task;
        this.eventParameter = eventParameter;
        this.multiParameterTask = null;
        this.syncer = null;
    }

    public TaskRunnable(Task<T> task, Syncer syncer, T eventParameter) {
        this.task = task;
        this.eventParameter = eventParameter;
        this.syncer = syncer;
        this.multiParameterTask = null;
        this.syncer = null;
    }

    public TaskRunnable(MultiParameterTask<T> task) {
        this.task = null;
        this.multiParameterTask = task;
        this.syncer = null;
    }

    public TaskRunnable(MultiParameterTask<T> task, Syncer syncer) {
        this.task = null;
        this.multiParameterTask = task;
        this.syncer = syncer;
    }

    public TaskRunnable(T parameter, MultiParameterTask<T> task) {
        this.task = null;
        this.multiParameterTask = task;
        this.syncer = null;
        this.parameters.add(parameter);
    }

    public TaskRunnable(MultiParameterTask<T> task, Syncer syncer, T parameter) {
        this.task = null;
        this.multiParameterTask = task;
        this.syncer = syncer;
        this.parameters.add(parameter);
    }

    public TaskRunnable(T[] parameters, MultiParameterTask<T> task) {
        this.task = null;
        this.multiParameterTask = task;
        this.syncer = null;
        this.parameters.addAll(Arrays.asList(parameters));
    }

    public TaskRunnable(MultiParameterTask<T> task, Syncer syncer, T[] parameters) {
        this.task = null;
        this.multiParameterTask = task;
        this.syncer = syncer;
        this.parameters.addAll(Arrays.asList(parameters));
    }

    @Override
    public void run() {
        try {
            if (task != null) task.invoke(eventParameter);
            else if (multiParameterTask != null) multiParameterTask.invoke(parameters);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (syncer != null) syncer.countDown();
    }

}

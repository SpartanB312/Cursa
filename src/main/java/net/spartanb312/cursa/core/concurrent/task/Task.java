package net.spartanb312.cursa.core.concurrent.task;

/**
 * Created by B_312 on 05/01/2021
 */
public interface Task<T> {
    void invoke(T valueIn);
}

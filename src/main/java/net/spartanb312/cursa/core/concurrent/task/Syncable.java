package net.spartanb312.cursa.core.concurrent.task;

import net.spartanb312.cursa.core.concurrent.utils.Syncer;

abstract class Syncable implements Runnable {
    protected Syncer syncer;
}

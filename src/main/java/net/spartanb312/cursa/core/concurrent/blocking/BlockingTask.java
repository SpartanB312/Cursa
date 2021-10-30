package net.spartanb312.cursa.core.concurrent.blocking;

import net.spartanb312.cursa.core.concurrent.task.Task;

public interface BlockingTask extends Task<BlockingContent> {
    @Override
    void invoke(BlockingContent unit);
}

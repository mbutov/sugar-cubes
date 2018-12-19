package org.sugarcubes.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author Maxim Butov
 */
class ExecutorServiceAdapter implements AsyncExecutor  {

    private final ExecutorService executor;

    public ExecutorServiceAdapter(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public <V> Future<V> submit(Callable<V> task) throws RejectedExecutionException {
        return executor.submit(task);
    }
}

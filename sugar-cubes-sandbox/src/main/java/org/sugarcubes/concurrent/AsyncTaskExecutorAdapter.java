package org.sugarcubes.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskRejectedException;

/**
 * @author Maxim Butov
 */
public class AsyncTaskExecutorAdapter implements AsyncExecutor {

    private final AsyncTaskExecutor executor;

    public AsyncTaskExecutorAdapter(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    @Override
    public <V> Future<V> submit(Callable<V> task) throws RejectedExecutionException {
        try {
            return executor.submit(task);
        }
        catch (TaskRejectedException e) {
            throw (RejectedExecutionException) e.getCause();
        }
    }

}

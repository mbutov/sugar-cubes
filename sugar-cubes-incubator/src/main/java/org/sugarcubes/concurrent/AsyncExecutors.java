package org.sugarcubes.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;

import org.springframework.core.task.AsyncTaskExecutor;

/**
 * @author Maxim Butov
 */
public class AsyncExecutors {

    public static AsyncExecutor of(ExecutorService executor) {
        return new ExecutorServiceAdapter(executor);
    }

    public static AsyncExecutor of(AsyncTaskExecutor executor) {
        return new AsyncTaskExecutorAdapter(executor);
    }

    public static AsyncExecutor sync() {
        return new AsyncExecutor() {
            @Override
            public <V> Future<V> submit(Callable<V> task) throws RejectedExecutionException {
                return new FutureTask<V>(task);
            }
        };
    }

}

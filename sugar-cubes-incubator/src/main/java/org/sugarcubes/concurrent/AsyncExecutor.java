package org.sugarcubes.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Consumer;

/**
 * An abstraction of asynchronous executor service,
 * the mimimalistic version of {@link org.springframework.core.task.AsyncTaskExecutor}.
 *
 * @author Maxim Butov
 */
public interface AsyncExecutor {

    /**
     * Submit a Callable task for execution, receiving a Future representing that task.
     * The Future will return the Callable's result upon completion.
     * @param task the {@code Callable} to execute (never {@code null})
     * @return a Future representing pending completion of the task
     * @throws RejectedExecutionException if the given task was not accepted
     *
     * @see java.util.concurrent.ExecutorService#submit(java.util.concurrent.Callable)
     * @see org.springframework.core.task.AsyncTaskExecutor#submit(java.util.concurrent.Callable)
     */
    <V> Future<V> submit(Callable<V> task) throws RejectedExecutionException;

    /**
     * Calls {@link #submit(Callable)} and then, if succeeded, sends result into {@code onSuccess}
     * listener.
     *
     * @param task the {@code Callable} to execute (never {@code null})
     * @param onSuccess listener called after successful execution of the task
     * @return a Future representing pending completion of the task
     * @throws RejectedExecutionException if the given task was not accepted
     */
    default <T> Future<T> submit(Callable<T> task, Consumer<T> onSuccess) {
        Consumer<Exception> empty = e -> {
        };
        return submit(task, onSuccess, empty);
    }

    /**
     * Calls {@link #submit(Callable)} and then, if succeeded, sends result into {@code onSuccess}
     * listener, if failed, sends exception into {@code onFailure} listener.
     *
     * @param task the {@code Callable} to execute (never {@code null})
     * @param onSuccess listener called after successful execution of the task
     * @param onFailure listener called if exception is thrown by the task
     * @return a Future representing pending completion of the task
     * @throws RejectedExecutionException if the given task was not accepted
     */
    default <T> Future<T> submit(Callable<T> task, Consumer<T> onSuccess, Consumer<Exception> onFailure) {
        return submit(() -> {
            T result;
            try {
                result = task.call();
            }
            catch (Exception e) {
                onFailure.accept(e);
                return null;
            }
            onSuccess.accept(result);
            return result;
        });
    }

}

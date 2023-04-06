package org.sugarcubes.thread;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Maxim Butov
 */
public class ThreadLocalsTest {

    @Test
    public void testSetGetRemove() {

        Thread thread = Thread.currentThread();
        ThreadLocal<Object> threadLocal = new ThreadLocal<>();

        Assertions.assertNull(threadLocal.get());
        Assertions.assertNull(ThreadLocals.get(thread, threadLocal));

        Assertions.assertNull(ThreadLocals.set(thread, threadLocal, 1));

        Assertions.assertEquals(1, threadLocal.get());
        Assertions.assertEquals(1, ThreadLocals.get(thread, threadLocal));

        ThreadLocals.remove(thread, threadLocal);

        Assertions.assertNull(threadLocal.get());
        Assertions.assertNull(ThreadLocals.get(thread, threadLocal));

    }

    @Test
    public void testThreadLocals() throws Exception {

        Thread thread = Thread.currentThread();
        Object monitor = new Object();

        ThreadLocal<Object> tl1 = new ThreadLocal<>();
        tl1.set(1);

        ThreadLocal<Object> tl2 = new ThreadLocal<>();

        Map<ThreadLocal<?>, Object> locals = ThreadLocals.getAll(thread, new ThreadLocal<>());

        MatcherAssert.assertThat(locals.keySet(), hasItem(tl1));
        MatcherAssert.assertThat(locals.keySet(), not(hasItem(tl2)));
        MatcherAssert.assertThat(tl1.get(), is(1));
        MatcherAssert.assertThat(tl2.get(), nullValue());

        AtomicReference<Map<ThreadLocal<?>, Object>> threadLocals = new AtomicReference<>();

        synchronized (monitor) {

            new Thread(() -> {
                synchronized (monitor) {

                    ThreadLocals.remove(thread, tl1);
                    ThreadLocals.set(thread, tl2, 2);

                    threadLocals.set(ThreadLocals.getAll(thread, new ThreadLocal<>()));

                    monitor.notify();

                }
            }).start();

            monitor.wait();
        }

        MatcherAssert.assertThat(threadLocals.get().keySet(), not(hasItem(tl1)));
        MatcherAssert.assertThat(threadLocals.get().keySet(), hasItem(tl2));
        MatcherAssert.assertThat(tl1.get(), nullValue());
        MatcherAssert.assertThat(tl2.get(), is(2));

    }

}

package org.sugarcubes.thread;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Assert;
import org.junit.Test;
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

        Assert.assertNull(threadLocal.get());
        Assert.assertNull(ThreadLocals.get(thread, threadLocal));

        Assert.assertNull(ThreadLocals.set(thread, threadLocal, 1));

        Assert.assertEquals(1, threadLocal.get());
        Assert.assertEquals(1, ThreadLocals.get(thread, threadLocal));

        ThreadLocals.remove(thread, threadLocal);

        Assert.assertNull(threadLocal.get());
        Assert.assertNull(ThreadLocals.get(thread, threadLocal));

    }

    @Test
    public void testThreadLocals() throws Exception {

        Thread thread = Thread.currentThread();
        Object monitor = new Object();

        ThreadLocal<Object> tl1 = new ThreadLocal<>();
        tl1.set(1);

        ThreadLocal<Object> tl2 = new ThreadLocal<>();

        Map<ThreadLocal<?>, Object> locals = ThreadLocals.getAll(thread, new ThreadLocal<>());

        Assert.assertThat(locals.keySet(), hasItem(tl1));
        Assert.assertThat(locals.keySet(), not(hasItem(tl2)));
        Assert.assertThat(tl1.get(), is(1));
        Assert.assertThat(tl2.get(), nullValue());

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

        Assert.assertThat(threadLocals.get().keySet(), not(hasItem(tl1)));
        Assert.assertThat(threadLocals.get().keySet(), hasItem(tl2));
        Assert.assertThat(tl1.get(), nullValue());
        Assert.assertThat(tl2.get(), is(2));

    }

}

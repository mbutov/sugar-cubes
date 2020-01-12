package org.sugarcubes.thread;

import java.util.Set;
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

    static class CustomThreadLocal extends ThreadLocal {

        final String name;

        public CustomThreadLocal(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "TL@"+name;
        }
    }

    @Test
    public void testThreadLocals() throws Exception {

        Thread thread = Thread.currentThread();
        Object monitor = new Object();

        ThreadLocal<Object> tl1 = new ThreadLocal<>();
        tl1.set("1");

        ThreadLocal<Object> tl2 = new ThreadLocal<>();

        AtomicReference<Set<ThreadLocal<?>>> threadLocals = new AtomicReference<>();

        synchronized (monitor) {

            new Thread(() -> {
                synchronized (monitor) {

                    ThreadLocals.remove(thread, tl1);
                    ThreadLocals.setValue(thread, tl2, "2");

                    threadLocals.set(ThreadLocals.getThreadLocals(thread, new ThreadLocal<>()));

                    monitor.notify();
                    
                }
            }).start();

            monitor.wait();
        }

        Assert.assertThat(threadLocals.get(), not(hasItem(tl1)));
        Assert.assertThat(threadLocals.get(), hasItem(tl2));
        Assert.assertThat(tl1.get(), nullValue());
        Assert.assertThat(tl2.get(), is("2"));

    }

}

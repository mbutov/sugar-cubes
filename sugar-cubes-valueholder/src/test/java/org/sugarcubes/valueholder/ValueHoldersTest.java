package org.sugarcubes.valueholder;

import java.util.concurrent.Executors;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class ValueHoldersTest {

    @Test
    public void testGlobal() {

        final ValueHolder<Object> vh1 = ValueHolders.global("12" + 3);
        final ValueHolder<Object> vh2 = ValueHolders.global(1 + "23");

        Assert.assertNotSame(vh1, vh2);
        Assert.assertEquals(vh1, vh2);

        vh1.set(1);
        vh2.set(2);

        Assert.assertEquals(2, vh1.get());
        Assert.assertEquals(2, vh2.get());

        vh1.remove();

        Assert.assertNull(vh1.get());
        Assert.assertNull(vh2.get());

    }

    @Test
    public void testThreadLocals() {

        final ValueHolder<Object> vh1 = ValueHolders.thread("12" + 3);
        final ValueHolder<Object> vh2 = ValueHolders.thread(1 + "23");

        Assert.assertNotSame(vh1, vh2);
        Assert.assertEquals(vh1, vh2);

        Executors.newSingleThreadExecutor().execute(() -> {
            for (int k = 0; k < 10; k++) {
                vh1.set(k);
                delay(10);
                Assert.assertEquals(k, vh1.get());
            }
        });
        Executors.newSingleThreadExecutor().execute(() -> {
            for (int k = 0; k < 10; k++) {
                vh1.set(-k);
                delay(10);
                Assert.assertEquals(-k, vh1.get());
            }
        });

        delay(1000);
        Assert.assertNull(vh1.get());
    }

    @Test
    public void testThreadSingletons() {

        final ValueHolder<Object> vh1 = ValueHolders.threadSingleton(Object.class);
        final ValueHolder<Object> vh2 = ValueHolders.threadSingleton(Object.class);

        Assert.assertNotSame(vh1, vh2);
        Assert.assertEquals(vh1, vh2);

        Executors.newSingleThreadExecutor().execute(() -> {
            for (int k = 0; k < 10; k++) {
                vh1.set(k);
                delay(10);
                Assert.assertEquals(k, vh1.get());
            }
        });
        Executors.newSingleThreadExecutor().execute(() -> {
            for (int k = 0; k < 10; k++) {
                vh1.set(-k);
                delay(10);
                Assert.assertEquals(-k, vh1.get());
            }
        });

        delay(150);
        Assert.assertNull(vh1.get());
        Assert.assertNotNull(vh1.getOrCreate());
    }

    private static void delay(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}

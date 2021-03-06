package org.sugarcubes.cloner;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Maxim Butov
 */
public abstract class AbstractClonerTests {

    static class A implements Serializable {

        final int x = 1;
        final String y = "2";
        final Object z = null;

        byte[] xxx = {1, 2, 3,};
        Object[] yyy = {1, "2", 3.0, new int[] {4}, Collections.singletonMap(5, 5.0),};
        Object[] zzz = new Object[2];

        {
            zzz[0] = this;
            zzz[1] = zzz;
        }
    }

    static class B extends A {

        A a = this;
        int n = 5;

        B(int i) {
        }

    }

    protected abstract Cloner getCloner();

    @Test
    public void testCloner() {

        Cloner cloner = getCloner();

        Assert.assertNull(cloner.clone(null));

        B b1 = new B(1);
        B b2 = cloner.clone(b1);

        Assert.assertEquals(b1.x, b2.x);
        Assert.assertSame(b1.y, b2.y);
        Assert.assertNull(b2.z);
        Assert.assertSame(b2.a, b2);

        Assert.assertTrue(Arrays.equals(b1.xxx, b2.xxx));
        Assert.assertTrue(Arrays.deepEquals(b1.yyy, b2.yyy));
        Assert.assertSame(b2, b2.zzz[0]);
        Assert.assertSame(b2.zzz, b2.zzz[1]);

        Assert.assertSame(b2, b2.a);
        Assert.assertEquals(b1.n, b2.n);

    }

}

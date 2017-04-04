package org.sugarcubes.cloner;

import java.io.Serializable;
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

        byte[] xxx = {1, 2, 3,};
        Object[] yyy = {1, "2", 3.0, new int[] {4}, Collections.singletonMap(5, 5.0),};

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

        B b1 = new B(1);
        B b2 = getCloner().clone(b1);

        Assert.assertEquals(b1.x, b2.x);
        Assert.assertSame(b1.y, b2.y);

        Assert.assertArrayEquals(b1.xxx, b2.xxx);
        Assert.assertArrayEquals(b1.yyy, b2.yyy);

        Assert.assertSame(b2, b2.a);
        Assert.assertEquals(b1.n, b2.n);

    }

}

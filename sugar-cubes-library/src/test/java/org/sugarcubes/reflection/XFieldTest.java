package org.sugarcubes.reflection;

import java.lang.reflect.Modifier;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class XFieldTest {

    class A {

        int a;

    }

    @Test
    public void testFieldModifiers() {
        XField<Integer> a1 = XReflection.of(A.class).getDeclaredField("a");
        XField<Integer> a2 = a1.withModifiers(Modifier.PUBLIC);
        Assert.assertEquals(a1, a2);
        Assert.assertNotSame(a1.getReflectionObject(), a2.getReflectionObject());
    }

}
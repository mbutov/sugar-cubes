package org.sugarcubes.s2;

import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ZSerialVerCalculatorTest {

    static class A implements Serializable {
        private static final long serialVersionUID = 1L;
    }

    static class B implements Serializable {
    }

    static class C {
    }

    @Test
    void testSerialVersionUids() throws Exception {

        Method computeDefaultSUID = ObjectStreamClass.class.getDeclaredMethod("computeDefaultSUID", Class.class);
        computeDefaultSUID.setAccessible(true);

        Object proxyObj = Proxy.newProxyInstance(null, new Class[] {Cloneable.class}, (proxy, method, args) -> null);
        Class<?>[] classes = {A.class,B.class,C.class, proxyObj.getClass(),};

        for (Class<?> clazz : classes) {
            long suid1 = (Long) computeDefaultSUID.invoke(null, clazz);
            long suid2 = ZSerialVerCalculator.getSerialVersionUID(clazz);
            Assertions.assertEquals(suid1, suid2);
        }

    }

}
package org.sugarcubes.proxy;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Butov
 */
public class ProxyExceptionTest {

    interface X {

        void x();

    }

    @Test
    public void testProxyException() throws Throwable {

        X test = new JavaProxyProvider().getFactory(X.class).newProxy(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                throw new IOException("test");
            }
        });

        Assertions.assertThrows(RuntimeException.class, () -> test.x());
    }

}

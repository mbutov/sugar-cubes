package org.sugarcubes.reflection;

import org.junit.jupiter.api.Test;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XMethodTest {

    public class A<X, Y> {

        public Y method(X x) {
            return null;
        }

    }

    public class B extends A<Integer, String> {

        @Override
        public String method(Integer integer) {
            super.method(integer);
            return integer.toString();
        }

    }

    @Test
    public void testGetParent() {

        XMethod<String> aMethod1 = XReflection.of(A.class).getMethod("method", Object.class);
        XMethod<String> bMethod1 = XReflection.of(B.class).getMethod("method", Integer.class);

    }

}
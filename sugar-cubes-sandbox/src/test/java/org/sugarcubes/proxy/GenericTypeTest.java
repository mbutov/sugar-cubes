package org.sugarcubes.proxy;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Butov
 */
public class GenericTypeTest {

    interface A {

    }

    interface B {

    }

    interface C extends A {

    }

    interface D extends A, B {

    }

    class P implements A {

    }

    class Q extends P implements B {

    }

    class R extends Q implements C {

    }

    @Test
    public void testInheritance() {

        assertEqualsIgnoreOrder(GenericType.create(A.class).getInterfaces(), A.class);
        assertEqualsIgnoreOrder(GenericType.create(B.class).getInterfaces(), B.class);
        assertEqualsIgnoreOrder(GenericType.create(C.class).getInterfaces(), C.class);
        assertEqualsIgnoreOrder(GenericType.create(D.class).getInterfaces(), D.class);

        assertEqualsIgnoreOrder(GenericType.create(P.class, D.class).getInterfaces(), D.class);
        assertEqualsIgnoreOrder(GenericType.create(P.class, D.class).getAllInterfaces(), A.class, B.class, D.class);
        assertEqualsIgnoreOrder(GenericType.create(Q.class, B.class).getInterfaces());
        assertEqualsIgnoreOrder(GenericType.create(R.class).getInterfaces());

        Assertions.assertTrue(GenericType.create(A.class).isAssignableFrom(R.class));

        Assertions.assertTrue(GenericType.create(P.class).isAssignableFrom(R.class));
        Assertions.assertTrue(GenericType.create(P.class).isAssignableFrom(Q.class));
        Assertions.assertFalse(GenericType.create(P.class).isAssignableFrom(D.class));

        Assertions.assertTrue(GenericType.create(R.class).isOfType(P.class));
        Assertions.assertTrue(GenericType.create(Q.class).isOfType(P.class));
        Assertions.assertFalse(GenericType.create(D.class).isOfType(P.class));

    }

    @Test
    public void testUnmodifiable() {
        Assertions.assertThrows(UnsupportedOperationException.class, () ->
            GenericType.create(A.class).unmodifiable().implementing(B.class));
    }

    private <T> void assertEqualsIgnoreOrder(T[] actual, T... expected) {
        Assertions.assertEquals(new HashSet<T>(Arrays.asList(expected)), new HashSet<T>(Arrays.asList(actual)));
    }

}

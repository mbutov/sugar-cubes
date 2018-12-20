package org.sugarcubes.rex;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class Rex2Test {

    @Test(expected = NullPointerException.class)
    public void testRex2() {

        try {
            throw new InvocationTargetException(new NullPointerException("null"));
        } catch (Throwable error) {
            throw Rex2.of(error)
                .replaceIf(InvocationTargetException.class, Throwable::getCause)
                .rethrowIfUnchecked()
                .replace(RuntimeException::new)
                .rethrow();
        }

    }

}
package org.sugarcubes.concurrent;

import java.io.IOException;

import org.junit.Test;
import org.sugarcubes.executable.XCallable;

/**
 * @author Maxim Butov
 */
public class XCallableTest {

    @Test
    public void testOfs() throws Exception {

        XCallable<Object, RuntimeException> xc1 = Methods::objectNoException;
        XCallable<Object, IOException> xc2 = Methods::objectCheckedException;
        XCallable<Object, Exception> xc3 = Methods::objectException;

    }

}

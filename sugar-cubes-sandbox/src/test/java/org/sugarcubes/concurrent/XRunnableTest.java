package org.sugarcubes.concurrent;

import java.io.IOException;

import org.junit.Test;
import org.sugarcubes.executable.XRunnable;

/**
 * @author Maxim Butov
 */
public class XRunnableTest {

    @Test
    public void testOfs() throws Exception {

        XRunnable<RuntimeException> xr11 = Methods::voidNoException;
        XRunnable<IOException> xr12 = Methods::voidCheckedException;
        XRunnable<Exception> xr13 = Methods::voidException;

        XRunnable<RuntimeException> xr21 = Methods::objectNoException;
        XRunnable<IOException> xr22 = Methods::objectCheckedException;
        XRunnable<Exception> xr23 = Methods::objectException;

    }

}

package org.sugarcubes.concurrent;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * @author Maxim Butov
 */
public class ExecutableTest {

    @Test
    public void testOfs() throws Exception {

        Executable<Object, RuntimeException> ex11 = Executable.ofx(Methods::voidNoException);
        Executable<Object, IOException> ex12 = Executable.ofx(Methods::voidCheckedException);
        Executable<Object, Exception> ex13 = Executable.ofx(Methods::voidException);

        Executable<Object, RuntimeException> ex21 = Executable.ofx(Methods::objectNoException);
        Executable<Object, IOException> ex22 = Executable.ofx(Methods::objectCheckedException);
        Executable<Object, Exception> ex23 = Executable.ofx(Methods::objectException);

    }

}

package org.sugarcubes.rex;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Butov
 */
public class RexTest {

    @Test
    public void testTranslated() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            try {
                throw new IOException();
            }
            catch (Throwable e) {
                throw Rex.of(e).rethrowAsRuntime();
            }
        });
    }

    @Test
    public void testRuntimeException() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            try {
                throw new NullPointerException();
            }
            catch (Throwable e) {
                throw Rex.of(e).rethrowAsRuntime();
            }
        });
    }

    @Test
    public void testError() {
        Assertions.assertThrows(NoClassDefFoundError.class, () -> {
            try {
                throw new NoClassDefFoundError();
            }
            catch (Throwable e) {
                throw Rex.of(e).rethrowAsRuntime();
            }
        });
    }

    @Test
    public void testOriginal() throws Throwable {
        Assertions.assertThrows(IOException.class, () -> {
            try {
                throw new IOException();
            }
            catch (Throwable e) {
                throw Rex.of(e).rethrow();
            }
        });
    }

    @Test
    public void testOriginalUndeclared() {
        Assertions.assertThrows(IOException.class, () -> {
            try {
                throw new IOException();
            }
            catch (Throwable e) {
                throw Rex.of(e).rethrowUndeclared();
            }
        });
    }

}

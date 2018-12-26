package org.sugarcubes.rex;

import java.io.IOException;

import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class RexTest {

    @Test(expected = RuntimeException.class)
    public void testTranslated() {
        try {
            throw new IOException();
        }
        catch (Throwable e) {
            throw Rex.of(e).rethrowAsRuntime();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testRuntimeException() {
        try {
            throw new NullPointerException();
        }
        catch (Throwable e) {
            throw Rex.of(e).rethrowAsRuntime();
        }
    }

    @Test(expected = NoClassDefFoundError.class)
    public void testError() {
        try {
            throw new NoClassDefFoundError();
        }
        catch (Throwable e) {
            throw Rex.of(e).rethrowAsRuntime();
        }
    }

    @Test(expected = IOException.class)
    public void testOriginal() throws Throwable {
        try {
            throw new IOException();
        }
        catch (Throwable e) {
            throw Rex.of(e).rethrow();
        }
    }

    @Test(expected = IOException.class)
    public void testOriginalUndeclared() {
        try {
            throw new IOException();
        }
        catch (Throwable e) {
            throw Rex.of(e).rethrowUndeclared();
        }
    }

}

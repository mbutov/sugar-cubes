package org.sugarcubes.rex;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class ExtraTest {

    @Test
    public void testLegalArgument() throws Exception {
        new Extra()
            .map(IOException.class, RuntimeException.class)
            .map(Throwable.class, RuntimeException::new);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgument() throws Exception {
        new Extra()
            .map(Throwable.class, RuntimeException.class)
            .map(IOException.class, RuntimeException::new);
    }

    @Test(expected = UncheckedIOException.class)
    public void testTranslator() throws Exception {
        throw new Extra()
            .map(IOException.class, UncheckedIOException::new)
            .apply(new IOException());
    }

}

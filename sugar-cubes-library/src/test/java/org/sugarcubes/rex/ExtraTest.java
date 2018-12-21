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
        Extra<Throwable, RuntimeException> map = new Extra<Throwable, RuntimeException>()
            .map(IOException.class, RuntimeException::new)
            .map(Throwable.class, RuntimeException::new);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgument() throws Exception {
        Extra<Throwable, RuntimeException> map = new Extra<Throwable, RuntimeException>()
            .map(Throwable.class, RuntimeException::new)
            .map(IOException.class, RuntimeException::new);
    }

    @Test(expected = UncheckedIOException.class)
    public void testTranslator() throws Exception {
        throw new Extra<Throwable, RuntimeException>()
            .map(IOException.class, UncheckedIOException::new)
            .apply(new IOException());
    }

}

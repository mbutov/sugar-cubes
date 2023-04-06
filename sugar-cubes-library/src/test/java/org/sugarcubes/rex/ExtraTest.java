package org.sugarcubes.rex;

import java.io.IOException;
import java.io.UncheckedIOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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

    @Test
    public void testIllegalArgument() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            new Extra<Throwable, RuntimeException>()
                .map(Throwable.class, RuntimeException::new)
                .map(IOException.class, RuntimeException::new)
        );
    }

    @Test
    public void testTranslator() throws Exception {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            throw new Extra<Throwable, RuntimeException>()
                .map(IOException.class, UncheckedIOException::new)
                .apply(new IOException());
        });
    }

}

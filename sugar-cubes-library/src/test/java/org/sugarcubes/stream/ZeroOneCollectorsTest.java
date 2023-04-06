package org.sugarcubes.stream;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.sugarcubes.stream.ZeroOneCollectors.onlyElement;
import static org.sugarcubes.stream.ZeroOneCollectors.toOptional;

/**
 * @author Maxim Butov
 */
public class ZeroOneCollectorsTest {

    @Test
    public void testToOptional() {
        Assertions.assertFalse(Stream.empty().collect(toOptional()).isPresent());
        Assertions.assertEquals("x", Stream.of("x").collect(toOptional()).get());
        try {
            Stream.of("x", "y").collect(toOptional());
            Assertions.fail();
        }
        catch (IllegalStateException e) {
            // ok
        }
    }

    @Test
    public void testToOnlyElement() {
        try {
            Stream.empty().collect(onlyElement());
        }
        catch (NoSuchElementException e) {
            // ok
        }
        Assertions.assertEquals("x", Stream.of("x").collect(onlyElement()));
        try {
            Stream.of("x", "y").collect(onlyElement());
            Assertions.fail();
        }
        catch (IllegalStateException e) {
            // ok
        }
    }

}

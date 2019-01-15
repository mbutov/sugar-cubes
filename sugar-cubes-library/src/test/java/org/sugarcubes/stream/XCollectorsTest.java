package org.sugarcubes.stream;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import static org.sugarcubes.stream.ZeroOneCollectors.onlyElement;
import static org.sugarcubes.stream.ZeroOneCollectors.toOptional;

/**
 * @author Maxim Butov
 */
public class XCollectorsTest {

    @Test
    public void testToOptional() {
        Assert.assertFalse(Stream.empty().collect(toOptional()).isPresent());
        Assert.assertEquals("x", Stream.of("x").collect(toOptional()).get());
        try {
            Stream.of("x", "y").collect(toOptional());
            Assert.fail();
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
        Assert.assertEquals("x", Stream.of("x").collect(onlyElement()));
        try {
            Stream.of("x", "y").collect(onlyElement());
            Assert.fail();
        }
        catch (IllegalStateException e) {
            // ok
        }
    }

}

package org.sugarcubes.reflection;

import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class XCollectorsTest {

    @Test
    public void testToOptional() {
        Assert.assertFalse(Stream.empty().collect(XCollectors.toOptional()).isPresent());
        Assert.assertEquals(Integer.valueOf(0), Stream.of(0).collect(XCollectors.toOptional()).get());
        try {
            Stream.of(0, 1).collect(XCollectors.toOptional());
            Assert.fail();
        }
        catch (IllegalStateException e) {
            // ok
        }
    }

    @Test
    public void testToOnlyElement() {
        try {
            Stream.empty().collect(XCollectors.toOnlyElement());
        }
        catch (IllegalStateException e) {
            // ok
        }
        Assert.assertEquals(Integer.valueOf(0), Stream.of(0).collect(XCollectors.toOnlyElement()));
        try {
            Stream.of(0, 1).collect(XCollectors.toOnlyElement());
            Assert.fail();
        }
        catch (IllegalStateException e) {
            // ok
        }
    }

}

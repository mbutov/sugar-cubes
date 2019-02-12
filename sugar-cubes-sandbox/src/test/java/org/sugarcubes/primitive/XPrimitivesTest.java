package org.sugarcubes.primitive;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.Matchers.is;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XPrimitivesTest {

    @Test
    public void testCast() {

        Assert.assertThat(XPrimitives.BYTE.cast((long) 1), is((byte) 1));
        Assert.assertThat(XPrimitives.SHORT.cast((float) 1.0), is((short) 1));

    }

}

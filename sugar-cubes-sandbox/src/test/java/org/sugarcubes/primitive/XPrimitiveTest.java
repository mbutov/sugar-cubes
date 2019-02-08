package org.sugarcubes.primitive;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.Matchers.is;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XPrimitiveTest {

    @Test
    public void testCast() {

        Assert.assertThat(XPrimitive.BYTE.cast((long) 1), is((byte) 1));

        Assert.assertThat(XPrimitive.CHARACTER.cast((int) 65), is((char) 'A'));

        Assert.assertThat(XPrimitive.SHORT.cast((float) 1.0), is((short) 1));

    }

}

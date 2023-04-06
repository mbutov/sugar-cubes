package org.sugarcubes.primitive;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.is;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XPrimitivesTest {

    @Test
    public void testCast() {

        MatcherAssert.assertThat(XPrimitives.BYTE.cast((long) 1), is((byte) 1));
        MatcherAssert.assertThat(XPrimitives.SHORT.cast((float) 1.0), is((short) 1));

    }

}

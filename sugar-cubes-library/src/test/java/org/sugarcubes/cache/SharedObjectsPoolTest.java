package org.sugarcubes.cache;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * @author Maxim Butov
 */
public class SharedObjectsPoolTest {

    @Test
    public void testSharedObjects() {
        assertNull(SharedObjectsPool.global(null));
        assertSame(SharedObjectsPool.global(new Integer(1)), SharedObjectsPool.global(new Integer(1)));
        assertSame(SharedObjectsPool.global(new String("str")), SharedObjectsPool.global(new String("str")));
    }

}

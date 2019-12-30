package org.sugarcubes.cache;

import org.junit.Test;
import static org.junit.Assert.*;

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
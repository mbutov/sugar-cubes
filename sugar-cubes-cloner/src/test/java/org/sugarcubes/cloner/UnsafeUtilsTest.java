package org.sugarcubes.cloner;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class UnsafeUtilsTest {

    @Test
    public void testGetUnsafe() throws Exception {
        Assert.assertNotNull(UnsafeUtils.getUnsafe());
    }

}

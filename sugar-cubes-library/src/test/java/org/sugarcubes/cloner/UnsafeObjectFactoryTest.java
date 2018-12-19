package org.sugarcubes.cloner;

import org.junit.*;

/**
 * @author Maxim Butov
 */
public class UnsafeObjectFactoryTest {

    private class Xxx {

        private Xxx(int x) {
        }

    }

    @Test
    public void testNewInstance() throws Exception {
        Xxx xxx = new UnsafeObjectFactory().newInstance(Xxx.class);
        Assert.assertNotNull(xxx);
    }

}

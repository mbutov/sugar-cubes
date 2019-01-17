package org.sugarcubes.reflection;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.sugarcubes.cloner.Cloners;

/**
 * @author Maxim Butov
 */
public class XClassCacheTest {

    @Test
    public void testSerialization() {

        Map map1 = new XClassCache<>();
        map1.put(1, 2);

        Map map2 = Cloners.serializationClone(map1);
        Assert.assertTrue(map2.isEmpty());

    }

}

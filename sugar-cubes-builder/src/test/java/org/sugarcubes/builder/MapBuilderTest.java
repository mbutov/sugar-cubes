package org.sugarcubes.builder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class MapBuilderTest {

    @Test
    public void testMap() throws Exception {

        Map<Integer, String> map = MapBuilder.<Integer, String>treeMap()
            .put(2, "two")
            .put(1, "one")
            .put(3, "three")
            .get();

        Assert.assertEquals(new HashSet(Arrays.asList(1, 2, 3)), map.keySet());
        Assert.assertArrayEquals(Arrays.asList("one", "two", "three").toArray(), map.values().toArray());
        
    }

}

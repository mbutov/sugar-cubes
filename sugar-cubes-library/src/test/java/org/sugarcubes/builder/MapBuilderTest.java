package org.sugarcubes.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeSet;

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
            .build();

        Assert.assertEquals(new TreeSet<>(Arrays.asList(1, 2, 3)), map.keySet());
        Assert.assertEquals(Arrays.asList("one", "two", "three"), new ArrayList<>(map.values()));
        
    }

}

package org.sugarcubes.builder.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
            .put(2, "two too")
            .build();

        Assertions.assertEquals(new TreeSet<>(Arrays.asList(1, 2, 3)), map.keySet());
        Assertions.assertEquals(Arrays.asList("one", "two too", "three"), new ArrayList<>(map.values()));

    }

    @Test
    public void testDuplicateKeys() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            MapBuilder.<String, String>hashMap().errorOnDuplicateKeys()
                .put("one", "two")
                .put("one", "three")
                .build()
        );
    }

}

package org.sugarcubes.builder.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Butov
 */
public class ListBuilderTest {

    @Test
    public void testArrayList() throws Exception {

        List<String> list = ListBuilder.<String>arrayList()
            .add("x")
            .addAll("y", "z")
            .build();

        Assertions.assertTrue(list instanceof ArrayList);
        Assertions.assertEquals(Arrays.asList("x", "y", "z"), list);

    }

    @Test
    public void testUnmodifyableList() throws Exception {

        List<String> list = ListBuilder.<String>linkedList()
            .add("x")
            .addAll("y", "z")
            .replace(Collections::unmodifiableList)
            .build();

        Assertions.assertEquals(Arrays.asList("x", "y", "z"), list);

        Assertions.assertThrows(UnsupportedOperationException.class, () -> list.add("1"));

    }

    @Test
    public void testArray() throws Exception {
        Integer[] array = ListBuilder.<Integer>arrayList().addAll(1, 2, 3, 4, 5).toArray(Integer[]::new);
        Assertions.assertArrayEquals(new Integer[] {1, 2, 3, 4, 5,}, array);
    }

}

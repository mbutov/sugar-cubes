package org.sugarcubes.builder.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.sugarcubes.builder.collection.ListBuilder;

/**
 * @author Maxim Butov
 */
public class ListBuilderTest {

    @Test
    public void testArrayList() throws Exception {

        List<String> list = ListBuilder.<String>arrayList()
            .add("x")
            .add("y", "z")
            .build();

        Assert.assertTrue(list instanceof ArrayList);
        Assert.assertEquals(Arrays.asList("x", "y", "z"), list);

    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnmodifyableList() throws Exception {

        List<String> list = ListBuilder.<String>linkedList()
            .add("x")
            .add("y", "z")
            .replace(Collections::unmodifiableList)
            .build();

        Assert.assertEquals(Arrays.asList("x", "y", "z"), list);

        list.add("1");

    }

    @Test
    public void testArray() throws Exception {
        Integer[] array = ListBuilder.<Integer>arrayList().add(1, 2, 3, 4, 5).toArray(n -> new Integer[n]);
        Assert.assertArrayEquals(new Integer[] {1, 2, 3, 4, 5,}, array);
    }

}

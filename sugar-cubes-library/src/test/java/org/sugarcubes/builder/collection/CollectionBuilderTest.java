package org.sugarcubes.builder.collection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Test;
import org.sugarcubes.builder.collection.CollectionBuilder;

/**
 * @author Maxim Butov
 */
public class CollectionBuilderTest {

    @Test
    public void testCollection() throws Exception {

        Collection<Integer> collection = CollectionBuilder.<Integer, Collection<Integer>>collection(ArrayList::new)
            .add(1).add(2).addAll(3, 4, 5)
            .replace(Collections::unmodifiableCollection)
            .build();

        Assert.assertArrayEquals(new Object[] {1, 2, 3, 4, 5,}, collection.toArray());

    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnmodifiableCollection() throws Exception {

        new CollectionBuilder<>(HashSet::new)
            .cast()
            .replace(Collections::unmodifiableCollection)
            .add(1)
            .build();

    }

}

package org.sugarcubes.builder.collection;

import java.util.Collections;

import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class SetBuilderTest {

    @Test
    public void testUnmodifiable() throws Exception {
        SetBuilder.<Integer>hashSet()
            .add(1).add(2)
            .replace(Collections::unmodifiableSet)
            .build();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnmodifiableCollection() throws Exception {

        SetBuilder.<Integer>hashSet()
            .replace(Collections::unmodifiableSet)
            .add(1)
            .build();

    }

}

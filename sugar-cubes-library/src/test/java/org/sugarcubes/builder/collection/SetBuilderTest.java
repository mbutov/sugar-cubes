package org.sugarcubes.builder.collection;

import java.util.Collections;

import org.junit.Test;
import org.sugarcubes.builder.collection.SetBuilder;

/**
 * @author Maxim Butov
 */
public class SetBuilderTest {

    @Test
    public void testUnmodifiable() throws Exception {
        SetBuilder.<Integer>hashSet()
            .add(1).add(2)
            .cast()
            .replace(Collections::unmodifiableCollection)
            .build();

    }

}

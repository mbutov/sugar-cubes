package org.sugarcubes.builder;

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
            .cast()
            .replace(Collections::unmodifiableCollection)
            .build();

    }

}

package org.sugarcubes.builder;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class BuildersTest {

    @Test
    public void testBuilders() {

        int y = Builders.of(1)
            .transform(x -> x + 1)
            .build();

        Assert.assertEquals(2, y);

    }
    
}

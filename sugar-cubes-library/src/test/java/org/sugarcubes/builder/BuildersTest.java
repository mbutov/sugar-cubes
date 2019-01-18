package org.sugarcubes.builder;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.Matchers.is;

/**
 * @author Maxim Butov
 */
public class BuildersTest {

    @Test
    public void testBuilders() {

        int y = Builders.of(1)
            .transform(x -> x + 1)
            .build();

        Assert.assertThat(y, is(2));

    }
    
}

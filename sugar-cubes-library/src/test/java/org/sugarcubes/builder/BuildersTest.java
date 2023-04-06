package org.sugarcubes.builder;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
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

        MatcherAssert.assertThat(y, is(2));

    }
    
}

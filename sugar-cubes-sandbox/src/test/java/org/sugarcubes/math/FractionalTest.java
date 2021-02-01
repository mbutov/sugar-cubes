package org.sugarcubes.math;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.sugarcubes.math.Fractional.parse;

/**
 * @author Maxim Butov
 */
public class FractionalTest {

    @Test
    public void testFractions() {
        Assert.assertThat(parse("1/4").multiply(parse("2")), is(parse("1/2")));
        Assert.assertThat(parse("2/3").multiply(parse("-7/9")), is(parse("+14/-27")));
        Assert.assertThat(parse("2/3").negate(), is(parse("-2/3")));
        Assert.assertThat(parse("0/1"), is(Fractional.ZERO));
        Assert.assertThat(parse("1/10", 2), is(parse("1/2")));
        Assert.assertThat(parse("-1/2").pow(2), is(parse("1/4")));
    }

}

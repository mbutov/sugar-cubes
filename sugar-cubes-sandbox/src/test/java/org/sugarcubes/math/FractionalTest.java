package org.sugarcubes.math;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.sugarcubes.math.Fractional.ZERO;
import static org.sugarcubes.math.Fractional.of;
import static org.sugarcubes.math.Fractional.parse;

/**
 * @author Maxim Butov
 */
public class FractionalTest {

    @Test
    public void testFractions() {

        Assert.assertThat(of(2, 6), is(of(1, 3)));
        Assert.assertThat(parse("-1/4"), is(of(1, -4)));
        Assert.assertThat(parse("0/1"), is(ZERO));

        try {
            parse("1/0");
            Assert.fail();
        }
        catch (ArithmeticException e) {
            // ok
        }

        Assert.assertThat(parse("1/4").add(parse("1/3")), is(parse("7/12")));
        Assert.assertThat(parse("1/4").multiply(parse("2")), is(parse("1/2")));
        Assert.assertThat(parse("2/3").multiply(parse("-7/9")), is(parse("+14/-27")));
        Assert.assertThat(parse("2/3").negate(), is(parse("-2/3")));
        Assert.assertThat(parse("1/10", 2), is(parse("1/2")));
        Assert.assertThat(parse("-1/2").pow(2), is(parse("1/4")));
        Assert.assertThat(parse("-1/2").pow(3), is(parse("1/-8")));
        Assert.assertThat(parse("3/2").getWholePart(), is(new BigInteger("1")));
        Assert.assertThat(parse("-3/2").getWholePart(), is(new BigInteger("-1")));

        System.out.println(parse("3/2").getFractionalPart());
        System.out.println(parse("-3/2").getFractionalPart());

    }

}

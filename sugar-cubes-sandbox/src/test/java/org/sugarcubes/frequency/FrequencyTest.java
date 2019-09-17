package org.sugarcubes.frequency;

import org.junit.Test;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;
import static org.sugarcubes.frequency.Frequency.perDay;
import static org.sugarcubes.frequency.Frequency.perHour;
import static org.sugarcubes.frequency.Frequency.perMinute;
import static org.sugarcubes.frequency.Frequency.perSecond;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class FrequencyTest {

    @Test
    public void testFrequencies() {

        assertThat(perHour(1), is(perDay(24)));
        assertThat(perMinute(1), is(perHour(60)));
        assertThat(perSecond(1), is(perMinute(60)));

        assertThat(Frequency.of(1, 3600), is(perHour(1)));

        assertThat(perMinute(59), lessThan(perMinute(60)));
        assertThat(perMinute(61), greaterThan(perMinute(60)));

        assertThat(perSecond(1).divide(60), is(perMinute(1)));
        assertThat(perMinute(1).multiply(60), is(perSecond(1)));

        assertThat(perSecond(1).getPerSecond(), is(1));
        assertThat(perSecond(1).getPerMinute(), is(60));
        assertThat(perSecond(1).getPerHour(), is(60 * 60));
        assertThat(perSecond(1).getPerDay(), is(24 * 60 * 60));

    }

}

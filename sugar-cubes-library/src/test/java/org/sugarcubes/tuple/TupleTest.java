package org.sugarcubes.tuple;

import org.junit.Assert;
import org.junit.Test;
import org.sugarcubes.cloner.Cloners;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

/**
 * @author Maxim Butov
 */
public class TupleTest {

    @Test
    public void testEquals() {
        Assert.assertEquals(Tuples.of(1), Tuples.of(1));
        Assert.assertNotEquals(Tuples.of(1), Tuples.of("a"));
    }

    @Test
    public void testCompareTo() {
        Assert.assertThat(Tuples.of("a"), comparesEqualTo(Tuples.of("a")));
        Assert.assertThat(Tuples.of("a"), lessThan(Tuples.of("a", "b")));
        Assert.assertThat(Tuples.of("a", "a"), lessThan(Tuples.of("a", "b")));
        Assert.assertThat(Tuples.of("a", "b"), greaterThan(Tuples.of("a", "a")));
        Assert.assertThat(Tuples.of("a", "b"), greaterThan(Tuples.of("a")));
    }

    @Test(expected = NullPointerException.class)
    public void testNullElement() {
        Tuples.of(1, null);
    }

    @Test(expected = ClassCastException.class)
    public void testNonComparable() {
        Tuples.of(1).compareTo(Tuples.of("a"));
    }

    @Test
    public void testToArray() throws Exception {

        Tuple<Object> tuple = Tuples.of(1, 2, 3);

        assertArraySameTypeAndEqual(new Object[] {1, 2, 3}, tuple.toArray());
        assertArraySameTypeAndEqual(new Integer[] {1, 2, 3}, tuple.toArray(new Integer[0]));
        assertArraySameTypeAndEqual(new Integer[] {1, 2, 3}, tuple.toArray(new Integer[10]));

        Integer[] array = new Integer[tuple.size()];
        Assert.assertSame(array, tuple.toArray(array));
        Assert.assertArrayEquals(new Integer[] {1, 2, 3}, array);
    }

    public static void assertArraySameTypeAndEqual(Object[] expected, Object[] actual) {
        Assert.assertEquals(expected.getClass(), actual.getClass());
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testEmpty() {
        Empty<String> stringEmpty = Empty.instance();
        Empty<Integer> integerEmpty = Empty.instance();
        Assert.assertSame(stringEmpty, integerEmpty);
        Empty<String> stringEmpty2 = Cloners.serializationClone(stringEmpty);
        Assert.assertSame(stringEmpty, stringEmpty2);
    }

}

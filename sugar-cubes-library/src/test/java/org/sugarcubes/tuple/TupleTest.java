package org.sugarcubes.tuple;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
        Assertions.assertEquals(Tuples.of(1), Tuples.of(1));
        Assertions.assertNotEquals(Tuples.of(1), Tuples.of("a"));
    }

    @Test
    public void testCompareTo() {
        MatcherAssert.assertThat(Tuples.of("a"), comparesEqualTo(Tuples.of("a")));
        MatcherAssert.assertThat(Tuples.of("a"), lessThan(Tuples.of("a", "b")));
        MatcherAssert.assertThat(Tuples.of("a", "a"), lessThan(Tuples.of("a", "b")));
        MatcherAssert.assertThat(Tuples.of("a", "b"), greaterThan(Tuples.of("a", "a")));
        MatcherAssert.assertThat(Tuples.of("a", "b"), greaterThan(Tuples.of("a")));
    }

    @Test
    public void testNullElement() {
        Tuples.of(1, null);
    }

    @Test
    public void testNonComparable() {
        Assertions.assertThrows(ClassCastException.class, () ->
            Tuples.of(1).compareTo(Tuples.of("a")));
    }

    @Test
    public void testToArray() throws Exception {

        Tuple<Object> tuple = Tuples.of(1, 2, 3);

        assertArraySameTypeAndEqual(new Object[] {1, 2, 3}, tuple.toArray());
        assertArraySameTypeAndEqual(new Integer[] {1, 2, 3}, tuple.toArray(new Integer[0]));
        assertArraySameTypeAndEqual(new Integer[] {1, 2, 3, null, 0}, tuple.toArray(new Integer[] {0, 0, 0, 0, 0}));

        Integer[] array = new Integer[tuple.size()];
        Assertions.assertSame(array, tuple.toArray(array));
        Assertions.assertArrayEquals(new Integer[] {1, 2, 3}, array);
    }

    public static void assertArraySameTypeAndEqual(Object[] expected, Object[] actual) {
        Assertions.assertEquals(expected.getClass(), actual.getClass());
        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    public void testEmpty() {
        Empty<String> stringEmpty = Empty.instance();
        Empty<Integer> integerEmpty = Empty.instance();
        Assertions.assertSame(stringEmpty, integerEmpty);
        Empty<String> stringEmpty2 = Cloners.serializationClone(stringEmpty);
        Assertions.assertSame(stringEmpty, stringEmpty2);
    }

}

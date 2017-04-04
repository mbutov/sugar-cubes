package org.sugarcubes.tuple;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class TupleTest {

    @Test
    public void testEquals() throws Exception {
        Assert.assertEquals(new Tuple<Integer>(1), new Tuple<Object>(1));
        Assert.assertNotEquals(new Tuple<>(1), new Tuple<>("a"));
    }

    @Test
    public void testCompareTo() throws Exception {
        Assert.assertTrue(new Tuple<>("a").compareTo(new Tuple<>("a")) == 0);
        Assert.assertTrue(new Tuple<>("a").compareTo(new Tuple<>("a", "b")) == -1);
        Assert.assertTrue(new Tuple<>("a", "a").compareTo(new Tuple<>("a", "b")) == -1);
        Assert.assertTrue(new Tuple<>("a", "b").compareTo(new Tuple<>("a", "a")) == 1);
        Assert.assertTrue(new Tuple<>("a", "b").compareTo(new Tuple<>("a")) == 1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullElement() throws Exception {
        new Tuple<>(1, null);
    }

    @Test(expected = ClassCastException.class)
    public void testNonComparable() throws Exception {
        new Tuple(1).compareTo(new Tuple("a"));
    }

    @Test
    public void testToArray() throws Exception {

        Tuple<Object> tuple = new Tuple<>(1, 2, 3);

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

}

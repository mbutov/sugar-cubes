package org.sugarcubes.tuple;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class TupleTest {

    @Test
    public void testEquals() throws Exception {
        Assert.assertEquals(new TupleImpl<Integer>(1), new TupleImpl<Object>(1));
        Assert.assertNotEquals(new TupleImpl<>(1), new TupleImpl<>("a"));
    }

    @Test
    public void testCompareTo() throws Exception {
        Assert.assertTrue(new TupleImpl<>("a").compareTo(new TupleImpl<>("a")) == 0);
        Assert.assertTrue(new TupleImpl<>("a").compareTo(new TupleImpl<>("a", "b")) == -1);
        Assert.assertTrue(new TupleImpl<>("a", "a").compareTo(new TupleImpl<>("a", "b")) == -1);
        Assert.assertTrue(new TupleImpl<>("a", "b").compareTo(new TupleImpl<>("a", "a")) == 1);
        Assert.assertTrue(new TupleImpl<>("a", "b").compareTo(new TupleImpl<>("a")) == 1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullElement() throws Exception {
        new TupleImpl<>(1, null);
    }

    @Test(expected = ClassCastException.class)
    public void testNonComparable() throws Exception {
        new TupleImpl(1).compareTo(new TupleImpl("a"));
    }

    @Test
    public void testToArray() throws Exception {

        TupleImpl<Object> tuple = new TupleImpl<>(1, 2, 3);

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
        Empty<String> stringEmpty2 = SerializationUtils.clone(stringEmpty);
        Assert.assertSame(stringEmpty, stringEmpty2);
    }

}

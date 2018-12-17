package org.sugarcubes;

import org.junit.Assert;
import org.junit.Test;

/**
 * todo: document it and adjust author
 *
 * @author Q-MBU
 */
public class EqualsHelperTest {

    class A {

        int value;

        A(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsHelper.equalsIsInstance(this, obj, that -> this.value == that.value);
        }

    }

    class B extends A {

        B(int value) {
            super(value);
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsHelper.equalsSameClass(this, obj, that -> this.value == that.value);
        }

    }

    @Test
    public void equalsTest() {
        Assert.assertEquals(new A(1), new A(1));
        Assert.assertEquals(new A(1), new B(1));
        Assert.assertNotEquals(new B(1), new A(1));
        Assert.assertEquals(new B(1), new B(1));
    }

}
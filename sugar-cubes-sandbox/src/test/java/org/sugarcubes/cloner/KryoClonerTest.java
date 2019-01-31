package org.sugarcubes.cloner;

import org.junit.Test;

import com.esotericsoftware.kryo.Kryo;

/**
 * @author Maxim Butov
 */
public class KryoClonerTest extends AbstractClonerTests {

    @Override
    protected Cloner getCloner() {
        return new KryoCloner();
    }

    @Test
    public void testRecursiveArray() {

        Object[] array = new Object[1];
        array[0] = array;

        Object[] copy = new Kryo().copy(array);

    }

}

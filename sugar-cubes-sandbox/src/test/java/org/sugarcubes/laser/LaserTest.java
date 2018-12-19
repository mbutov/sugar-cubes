package org.sugarcubes.laser;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.SerializationUtils;

/**
 * @author Maxim Butov
 */
public class LaserTest {

    public LaserTest() {}

    private static Object supplierMethod() {
        return new Object();
    }

    private static void comsumerMethod(Object arg) {
    }

    private static Object functionMethod(Object arg) {
        return arg;
    }

    interface SSuplier extends Supplier, Serializable {}
    interface SConsumer extends Consumer, Serializable {}
    interface SFunction extends Function, Serializable {}

    private final Supplier lambda1 = LaserTest::supplierMethod;
    private final Consumer lambda2 = LaserTest::comsumerMethod;
    private final Supplier lambda3 = () -> new Object();

    private final Supplier sLambda1 = (Supplier & Serializable) LaserTest::supplierMethod;
    private final Consumer sLambda2 = (Consumer & Serializable) LaserTest::comsumerMethod;
    private final Supplier sLambda3 = (Supplier & Serializable) () -> new Object();

    private final Consumer innerAnonymous = new Consumer() {
        @Override
        public void accept(Object o) {
        }
    };
    
    private final Serializable sInnerAnonymous = new Serializable() {
    };

    @Test
    public void testIsLambda() throws Exception {

        Assert.assertTrue(Laser.isLambda(lambda1));
        Assert.assertTrue(Laser.isLambda(lambda2));
        Assert.assertTrue(Laser.isLambda(lambda3));

        Assert.assertTrue(Laser.isLambda(sLambda1));
        Assert.assertTrue(Laser.isLambda(sLambda2));
        Assert.assertTrue(Laser.isLambda(sLambda3));

        Assert.assertFalse(Laser.isLambda(innerAnonymous));
        Assert.assertFalse(Laser.isLambda(sInnerAnonymous));

    }

    @Test
    public void testIsNonSerializableLambda() throws Exception {

        Assert.assertTrue(Laser.isNonSerializableLambda(lambda1));
        Assert.assertTrue(Laser.isNonSerializableLambda(lambda2));
        Assert.assertTrue(Laser.isNonSerializableLambda(lambda3));

        Assert.assertFalse(Laser.isNonSerializableLambda(sLambda1));
        Assert.assertFalse(Laser.isNonSerializableLambda(sLambda2));
        Assert.assertFalse(Laser.isNonSerializableLambda(sLambda3));

        Assert.assertFalse(Laser.isNonSerializableLambda(innerAnonymous));
        Assert.assertFalse(Laser.isNonSerializableLambda(sInnerAnonymous));

    }

    @Ignore
    @Test
    public void testSerializables() throws Exception {
        
        assertSerializable(sLambda1);
        assertSerializable(sLambda2);
        assertSerializable(sLambda3);

        assertSerializable(Laser.serializable(lambda1));
        assertSerializable(Laser.serializable(lambda2));
        assertSerializable(Laser.serializable(lambda3));

        assertSerializable(sInnerAnonymous);
        assertSerializable(Laser.serializable(innerAnonymous));

    }

    public void assertSerializable(Object lambda) {

        Object clone = SerializationUtils.deserialize(SerializationUtils.serialize(lambda));
        Assert.assertNotSame(clone, lambda);
        Assert.assertArrayEquals(lambda.getClass().getInterfaces(), clone.getClass().getInterfaces());

        clone.hashCode();
        clone.equals(lambda);
        clone.toString();

    }

}

package org.sugarcubes.laser;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

/**
 * @author Maxim Butov
 */
public class LaserTest {

    public LaserTest() {}

    private static Object supplierMethod() {
        return new Object();
    }

    private static void consumerMethod(Object arg) {
    }

    private static Object functionMethod(Object arg) {
        return arg;
    }

    private final Supplier<Object> lambda1 = LaserTest::supplierMethod;
    private final Consumer<Object> lambda2 = LaserTest::consumerMethod;
    private final Supplier<Object> lambda3 = Object::new;

    private final Supplier<Object> sLambda1 = (Supplier<Object> & Serializable) LaserTest::supplierMethod;
    private final Consumer<Object> sLambda2 = (Consumer<Object> & Serializable) LaserTest::consumerMethod;
    private final Supplier<Object> sLambda3 = (Supplier<Object> & Serializable) Object::new;

    private final Consumer<Object> innerAnonymous = new Consumer() {
        @Override
        public void accept(Object o) {
        }
    };

    private final Serializable sInnerAnonymous = new Serializable() {
    };

    @Test
    public void testIsLambda() throws Exception {

        Assertions.assertTrue(Laser.isLambda(lambda1));
        Assertions.assertTrue(Laser.isLambda(lambda2));
        Assertions.assertTrue(Laser.isLambda(lambda3));

        Assertions.assertTrue(Laser.isLambda(sLambda1));
        Assertions.assertTrue(Laser.isLambda(sLambda2));
        Assertions.assertTrue(Laser.isLambda(sLambda3));

        Assertions.assertFalse(Laser.isLambda(innerAnonymous));
        Assertions.assertFalse(Laser.isLambda(sInnerAnonymous));

    }

    @Test
    public void testIsNonSerializableLambda() throws Exception {

        Assertions.assertTrue(Laser.isNonSerializableLambda(lambda1));
        Assertions.assertTrue(Laser.isNonSerializableLambda(lambda2));
        Assertions.assertTrue(Laser.isNonSerializableLambda(lambda3));

        Assertions.assertFalse(Laser.isNonSerializableLambda(sLambda1));
        Assertions.assertFalse(Laser.isNonSerializableLambda(sLambda2));
        Assertions.assertFalse(Laser.isNonSerializableLambda(sLambda3));

        Assertions.assertFalse(Laser.isNonSerializableLambda(innerAnonymous));
        Assertions.assertFalse(Laser.isNonSerializableLambda(sInnerAnonymous));

    }

    @Disabled
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
        Assertions.assertNotSame(clone, lambda);
        Assertions.assertArrayEquals(lambda.getClass().getInterfaces(), clone.getClass().getInterfaces());

        clone.hashCode();
        clone.equals(lambda);
        clone.toString();

    }

}

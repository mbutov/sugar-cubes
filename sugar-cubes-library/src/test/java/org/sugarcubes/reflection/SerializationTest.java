package org.sugarcubes.reflection;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.SerializationUtils;

/**
 * todo: document it and adjust author
 *
 * @author Q-MBU
 */
public class SerializationTest {

    @Test
    public void testXClassIsSerilizable() {

        assertIsSerializable(XReflection.of(Integer.class));
        assertIsSerializable(XReflection.of(Integer.class).getConstructor(int.class));
        assertIsSerializable(XReflection.of(Integer.class).getField("value"));
        assertIsSerializable(XReflection.of(Integer.class).getMethod("intValue"));

    }

    private static void assertIsSerializable(Object obj1) {
        Object obj2 = SerializationUtils.deserialize(SerializationUtils.serialize(obj1));
        Assert.assertEquals(obj1, obj2);
    }

}

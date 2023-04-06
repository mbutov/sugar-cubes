package org.sugarcubes.reflection;

import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * todo: document it
 *
 * @author Maxim Butov
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
        assertEquals(obj1, obj2);
    }

}

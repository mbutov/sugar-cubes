package org.sugarcubes.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XObjectInputOutputStreamTest {

    static class X implements Serializable {

        int x = 1;
        int[] xx = {1, 2};

    }

    static class Y extends X {

        Object[] array = new Object[2];

        {
            array[0] = this;
            array[1] = array;
        }
        
    }

    @Test
    public void testWriteReadObject() throws Exception {

        Y y1 = new Y();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        new XObjectOutputStream(buffer).writeObject(y1);

        Y y2 = (Y) new XObjectInputStream(new ByteArrayInputStream(buffer.toByteArray())).readObject();

        Assertions.assertEquals(y1.x, y2.x);

    }

}

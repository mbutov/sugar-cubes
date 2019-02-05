package org.sugarcubes.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Assert;
import org.junit.Test;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XObjectInputOutputStreamTest {

    static class X {

        int x = 1;

    }

    static class Y extends X {

    }

    @Test
    public void testWriteReadObject() throws Exception {

        Y y1 = new Y();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        new XObjectOutputStream(buffer).writeObject(y1);

        Y y2 = (Y) new XObjectInputStream(new ByteArrayInputStream(buffer.toByteArray())).readObject();

        Assert.assertEquals(y1.x, y2.x);

    }

}
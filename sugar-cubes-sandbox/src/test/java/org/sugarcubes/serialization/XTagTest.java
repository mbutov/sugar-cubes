package org.sugarcubes.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Butov
 */
public class XTagTest {

    @Test
    public void testXTagSerialization() throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream(2);

        for (int n = 0; n < 0x8000; n++) {
            buffer.reset();
            XTag tag1 = new XTag(n);
            tag1.write(buffer);
            XTag tag2 = XTag.read(new ByteArrayInputStream(buffer.toByteArray()));
            Assertions.assertEquals(tag1, tag2);
        }

    }


}

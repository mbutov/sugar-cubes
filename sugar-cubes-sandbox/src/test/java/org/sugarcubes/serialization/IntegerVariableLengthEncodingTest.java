package org.sugarcubes.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Maxim Butov
 */
public class IntegerVariableLengthEncodingTest {

    @Test
    public void testFits32() {
        Assert.assertTrue(IntegerVariableLengthEncoding.fits32(0x00000000_00000000L));
        Assert.assertTrue(IntegerVariableLengthEncoding.fits32(0x00000000_7FFFFFFFL));
        Assert.assertTrue(IntegerVariableLengthEncoding.fits32(0xFFFFFFFF_80000000L));
        Assert.assertTrue(IntegerVariableLengthEncoding.fits32(0xFFFFFFFF_FFFFFFFFL));

        Assert.assertFalse(IntegerVariableLengthEncoding.fits32(0x00000000_80000000L));
        Assert.assertFalse(IntegerVariableLengthEncoding.fits32(0x00000000_FFFFFFFFL));
        Assert.assertFalse(IntegerVariableLengthEncoding.fits32(0xFFFFFFFF_00000000L));
        Assert.assertFalse(IntegerVariableLengthEncoding.fits32(0xFFFFFFFF_7FFFFFFFL));
    }

    @Test
    public void testWriteReadLong() throws IOException {

        testWriteReadLong(-1);
        testWriteReadLong(0);
        testWriteReadLong(1);
        testWriteReadLong(2);
        testWriteReadLong(Integer.MIN_VALUE);
        testWriteReadLong(Integer.MAX_VALUE);
        testWriteReadLong(Long.MIN_VALUE);
        testWriteReadLong(Long.MAX_VALUE);

        Random r = new Random();
        for (int k = 0; k < 1024 * 1024; k++) {
            testWriteReadLong(r.nextInt());
            testWriteReadLong(r.nextLong());
        }
    }

    private void testWriteReadLong(long value) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        IntegerVariableLengthEncoding.writeLong(buffer, value);
        long value2 = IntegerVariableLengthEncoding.readLong(new ByteArrayInputStream(buffer.toByteArray()));
        Assert.assertEquals(value, value2);
//        System.out.println(Long.toHexString(value) + ", " + buffer.toByteArray().length);
    }

}

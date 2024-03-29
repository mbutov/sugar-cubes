package org.sugarcubes.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.is;
import static org.sugarcubes.serialization.IntegerVariableLengthEncoding.numBits;

/**
 * @author Maxim Butov
 */
public class IntegerVariableLengthEncodingTest {

    private static int byteToInt(int b) {
        return (int) (byte) b;
    }

    @Test
    public void testNumBits() {

        MatcherAssert.assertThat(numBits(byteToInt(0b0_0000_0000)), is(1));
        MatcherAssert.assertThat(numBits(byteToInt(0b0_1111_1111)), is(1));

        MatcherAssert.assertThat(numBits(byteToInt(0b0_0000_0001)), is(2));
        MatcherAssert.assertThat(numBits(byteToInt(0b0_0000_0010)), is(3));

        MatcherAssert.assertThat(numBits(byteToInt(0b0_1111_1110)), is(2));
        MatcherAssert.assertThat(numBits(byteToInt(0b0_1111_1100)), is(3));

    }

    @Test
    public void testFits32() {
        Assertions.assertTrue(IntegerVariableLengthEncoding.fits32(0x00000000_00000000L));
        Assertions.assertTrue(IntegerVariableLengthEncoding.fits32(0x00000000_7FFFFFFFL));
        Assertions.assertTrue(IntegerVariableLengthEncoding.fits32(0xFFFFFFFF_80000000L));
        Assertions.assertTrue(IntegerVariableLengthEncoding.fits32(0xFFFFFFFF_FFFFFFFFL));

        Assertions.assertFalse(IntegerVariableLengthEncoding.fits32(0x00000000_80000000L));
        Assertions.assertFalse(IntegerVariableLengthEncoding.fits32(0x00000000_FFFFFFFFL));
        Assertions.assertFalse(IntegerVariableLengthEncoding.fits32(0xFFFFFFFF_00000000L));
        Assertions.assertFalse(IntegerVariableLengthEncoding.fits32(0xFFFFFFFF_7FFFFFFFL));
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
        IntegerVariableLengthEncoding.writeLongLeb128(buffer, value);
        long value2 = IntegerVariableLengthEncoding.readLongLeb128(new ByteArrayInputStream(buffer.toByteArray()));
        Assertions.assertEquals(value, value2);
//        System.out.println(Long.toHexString(value) + ", " + buffer.toByteArray().length);
    }

}

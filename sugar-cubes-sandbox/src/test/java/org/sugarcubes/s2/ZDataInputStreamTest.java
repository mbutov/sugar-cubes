package org.sugarcubes.s2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.FastByteArrayOutputStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ZDataInputStreamTest {

    @Test
    void testUtf() throws Exception {

        testUtf(new String(new char[] {0}));
        testUtf("");
        testUtf("abc");
        testUtf("абв");

        List<String> allCodePointsString = allCodePointsString(256);
        for (int i = 0; i < allCodePointsString.size(); i++) {
            String str = allCodePointsString.get(i);
            testUtf(str);
        }
    }

    void testUtf(String str) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        new DataOutputStream(buffer).writeUTF(str);

        byte[] bytes = buffer.toByteArray();

        String str1 = new DataInputStream(new ByteArrayInputStream(bytes)).readUTF();
        assertEquals(str, str1);

        String str2 = new ZDataInputStream(new ByteArrayInputStream(bytes)).readUTF();
        assertEquals(str, str2);
    }

    List<String> allCodePointsString(int maxLength) {
        List<String> result = new ArrayList<>();

        StringBuilder buffer = new StringBuilder();
        for (int cp = Character.MIN_CODE_POINT; cp <= Character.MAX_CODE_POINT; cp++) {
            if (Character.isValidCodePoint(cp)) {
                buffer.append(Character.toString(cp));
            }
            if (buffer.length() >= maxLength) {
                result.add(buffer.toString());
                buffer.setLength(0);
            }
        }

        if (buffer.length() > 0) {
            result.add(buffer.toString());
        }

        return result;
    }

    @Test
    void testIntegers() throws IOException {

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Supplier<DataOutputStream> out = () -> {
            buffer.reset();
            return new DataOutputStream(buffer);
        };
        Supplier<ZDataInputStream> in = () -> {
            return new ZDataInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        };


        Random r = new Random();

        for (int k = 0; k < 10_000; k++) {

            boolean bool1 =  r.nextBoolean();
            out.get().writeBoolean(bool1);
            boolean bool2 = in.get().readBoolean();
            Assertions.assertEquals(bool1, bool2);

            byte b1 = (byte) r.nextInt();
            out.get().writeByte(b1);
            byte b2 = in.get().readByte();
            Assertions.assertEquals(b1, b2);

            char c1 = (char) r.nextInt();
            out.get().writeChar(c1);
            char c2 = in.get().readChar();
            Assertions.assertEquals(Integer.toHexString(c1), Integer.toHexString(c2));

            short s1 = (short) r.nextInt();
            out.get().writeShort(s1);
            short s2 = in.get().readShort();
            Assertions.assertEquals(Integer.toHexString(s1), Integer.toHexString(s2));

            int i1 = r.nextInt();
            out.get().writeInt(i1);
            int i2 = in.get().readInt();
            Assertions.assertEquals(Integer.toHexString(i1), Integer.toHexString(i2));

            long l1 = r.nextLong();
            out.get().writeLong(l1);
            long l2 = in.get().readLong();
            Assertions.assertEquals(i1, i2);

            float f1 = r.nextFloat() * 1e10f;
            out.get().writeFloat(f1);
            float f2 = in.get().readFloat();
            Assertions.assertEquals(f1, f2);

            double d1 = r.nextDouble() * 1e100;
            out.get().writeDouble(d1);
            double d2 = in.get().readDouble();
            Assertions.assertEquals(d1, d2);
        }
    }

}
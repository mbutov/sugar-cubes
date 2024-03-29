package org.sugarcubes.serialization.serializer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.sugarcubes.cache.WeakKeysCaches;
import org.sugarcubes.serialization.IntegerVariableLengthEncoding;
import org.sugarcubes.serialization.XObjectInputStream;
import org.sugarcubes.serialization.XObjectOutputStream;
import org.sugarcubes.serialization.XSerializer;

/**
 * @author Maxim Butov
 */
public class XStringSerializer implements XSerializer<String> {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private static final Map<String, byte[]> CACHE = WeakKeysCaches.weakValues();

    private static byte[] getBytes(String value) {
        return CACHE.computeIfAbsent(value, s -> s.getBytes(CHARSET));
    }

    @Override
    public boolean matches(XObjectOutputStream out, Object value) {
        return value instanceof String;
    }

    @Override
    public void writeValue(XObjectOutputStream out, String value) throws IOException {
        byte[] bytes = getBytes(value);
        IntegerVariableLengthEncoding.writeIntLeb128(out, bytes.length);
        out.write(bytes);
    }

    @Override
    public String create(XObjectInputStream in) throws IOException, ClassNotFoundException {
        int length = IntegerVariableLengthEncoding.readIntLeb128(in);
        byte[] bytes = new byte[length];
        int count = in.read(bytes);
        if (count != length) {
            throw new IOException("Cannot fully read string");
        }
        return new String(bytes, CHARSET);
    }

}

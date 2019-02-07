package org.sugarcubes.serialization.serializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.sugarcubes.serialization.XObjectInputStream;
import org.sugarcubes.serialization.XObjectOutputStream;
import org.sugarcubes.serialization.XSerializer;

/**
 * @author Maxim Butov
 */
public class XStringSerializer implements XSerializer<String> {

    @Override
    public boolean matches(XObjectOutputStream out, Object value) {
        return value instanceof String;
    }

    @Override
    public void writeValue(XObjectOutputStream out, String value) throws IOException {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        out.writeInt(bytes.length);
        out.write(bytes);
    }

    @Override
    public String create(XObjectInputStream in) throws IOException, ClassNotFoundException {
        int length = in.readInt();
        byte[] bytes = new byte[length];
        int count = in.read(bytes);
        if (count != length) {
            throw new IOException("Cannot fully read string");
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

}

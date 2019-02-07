package org.sugarcubes.serialization.serializer;

import java.io.IOException;

import org.sugarcubes.serialization.XObjectInputStream;
import org.sugarcubes.serialization.XObjectOutputStream;
import org.sugarcubes.serialization.XSerializer;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XPrimitiveSerializer<T> implements XSerializer<T> {

    private final Class<T> type;
    private final ValueWriter<T> writer;
    private final ValueReader<T> reader;

    public XPrimitiveSerializer(Class<T> type, ValueWriter<T> writer, ValueReader<T> reader) {
        this.type = type;
        this.writer = writer;
        this.reader = reader;
    }

    @Override
    public boolean matches(XObjectOutputStream out, Object value) {
        return type.isInstance(value);
    }

    @Override
    public void writeValue(XObjectOutputStream out, T value) throws IOException {
        writer.write(out, value);
    }

    @Override
    public T create(XObjectInputStream in) throws IOException, ClassNotFoundException {
        return reader.read(in);
    }

}

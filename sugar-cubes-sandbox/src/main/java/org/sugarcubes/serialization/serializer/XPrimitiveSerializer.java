package org.sugarcubes.serialization.serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.sugarcubes.serialization.XObjectInputStream;
import org.sugarcubes.serialization.XObjectOutputStream;
import org.sugarcubes.serialization.XSerializer;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XSimpleSerializer<T> implements XSerializer<T> {

    interface Writer<T> {

        void write(DataOutputStream out, T value) throws IOException;
    }

    interface Reader<T> {

        T read(DataInputStream in) throws IOException;
    }

    private final Class<T> type;
    private final Writer<T> writer;
    private final Reader<T> reader;

    public XSimpleSerializer(Class<T> type, Writer<T> writer, Reader<T> reader) {
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

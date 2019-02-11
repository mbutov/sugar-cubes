package org.sugarcubes.serialization.serializer;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.sugarcubes.function.TernaryConsumer;
import org.sugarcubes.serialization.XObjectInputStream;
import org.sugarcubes.serialization.XObjectOutputStream;
import org.sugarcubes.serialization.XSerializer;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XPrimitiveArraySerializer<A, T> implements XSerializer<A> {

    private final Class<A> arrayType;

    private final ValueWriter<T> writer;
    private final ValueReader<T> reader;

    private final Function<A, Integer> length;
    private final Function<Integer, A> factory;
    private final BiFunction<A, Integer, T> getter;
    private final TernaryConsumer<A, Integer, T> setter;

    public XPrimitiveArraySerializer(
        Class<A> arrayType,
        Function<A, Integer> length, Function<Integer, A> factory, BiFunction<A, Integer, T> getter, TernaryConsumer<A, Integer, T> setter,
        ValueWriter<T> writer, ValueReader<T> reader
    ) {
        this.arrayType = arrayType;
        this.writer = writer;
        this.reader = reader;
        this.length = length;
        this.factory = factory;
        this.getter = getter;
        this.setter = setter;
    }

    @Override
    public boolean matches(XObjectOutputStream out, Object value) {
        return arrayType.isInstance(value);
    }

    @Override
    public void writeValue(XObjectOutputStream out, A value) throws IOException {
        int length = this.length.apply(value);
        out.writeInt(length);
        for (int k = 0; k < length; k++) {
            writer.write(out, getter.apply(value, k));
        }
    }

    @Override
    public A create(XObjectInputStream in) throws IOException, ClassNotFoundException {
        int length = in.readInt();
        A array = factory.apply(length);
        for (int k = 0; k < length; k++) {
            setter.accept(array, k, reader.read(in));
        }
        return array;
    }

}

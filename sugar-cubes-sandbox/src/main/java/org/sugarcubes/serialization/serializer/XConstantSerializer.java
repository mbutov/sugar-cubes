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
public class XConstantSerializer<T> implements XSerializer<T> {

    private final T constant;

    public XConstantSerializer(T constant) {
        this.constant = constant;
    }

    @Override
    public boolean matches(XObjectOutputStream out, Object value) {
        return constant.equals(value);
    }

    @Override
    public void writeValue(XObjectOutputStream out, T value) throws IOException {
    }

    @Override
    public T create(XObjectInputStream in) throws IOException, ClassNotFoundException {
        return constant;
    }

}

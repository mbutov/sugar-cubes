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
public class XEnumSerializer<E extends Enum<E>> implements XSerializer<E> {

    @Override
    public boolean matches(XObjectOutputStream out, Object value) {
        return value instanceof Enum;
    }

    @Override
    public void writeValue(XObjectOutputStream out, E value) throws IOException {
        out.writeObject(value.getClass());
        out.writeObject(value.name());
    }

    @Override
    public E create(XObjectInputStream in) throws IOException, ClassNotFoundException {
        Class<E> enumType = in.readObject();
        String name = in.readObject();
        return Enum.valueOf(enumType, name);
    }

}

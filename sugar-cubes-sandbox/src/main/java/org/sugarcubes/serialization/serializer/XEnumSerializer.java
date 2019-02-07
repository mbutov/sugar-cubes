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
public class XEnumSerializer implements XSerializer<Enum> {

    @Override
    public boolean matches(XObjectOutputStream out, Object value) {
        return value instanceof Enum;
    }

    @Override
    public void writeValue(XObjectOutputStream out, Enum value) throws IOException {
        out.writeObject(value.getClass());
        out.writeObject(value.name());
    }

    @Override
    public Enum create(XObjectInputStream in) throws IOException, ClassNotFoundException {
        Class enumType = in.readObject();
        String name = in.readObject();
        return Enum.valueOf(enumType, name);
    }

}

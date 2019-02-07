package org.sugarcubes.serialization.serializer;

import java.io.IOException;
import java.lang.reflect.Array;

import org.sugarcubes.serialization.XObjectInputStream;
import org.sugarcubes.serialization.XObjectOutputStream;
import org.sugarcubes.serialization.XSerializer;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XArraySerializer implements XSerializer<Object[]> {

    @Override
    public boolean matches(XObjectOutputStream out, Object value) {
        return value instanceof Object[];
    }

    @Override
    public void writeValue(XObjectOutputStream out, Object[] value) throws IOException {
        out.writeObject(value.getClass().getComponentType());
        out.writeInt(value.length);
        for (int k = 0; k < value.length; k++) {
            out.writeObject(value[k]);
        }
    }

    @Override
    public Object[] create(XObjectInputStream in) throws IOException, ClassNotFoundException {
        Class componentType = in.readObject();
        int length = in.readInt();
        return (Object[]) Array.newInstance(componentType, length);
    }

    @Override
    public void readValue(XObjectInputStream in, Object[] value) throws IOException, ClassNotFoundException {
        for (int k = 0; k < value.length; k++) {
            value[k] = in.readObject();
        }
    }

}

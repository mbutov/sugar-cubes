package org.sugarcubes.serialization.serializer;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.objenesis.ObjenesisHelper;
import org.sugarcubes.reflection.XFieldAccessor;
import org.sugarcubes.reflection.XReflection;
import org.sugarcubes.serialization.XObjectInputStream;
import org.sugarcubes.serialization.XObjectOutputStream;
import org.sugarcubes.serialization.XSerializer;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XObjectSerializaer implements XSerializer<Object> {

    @Override
    public boolean matches(XObjectOutputStream out, Object value) {
        return true;
    }

    @Override
    public void writeValue(XObjectOutputStream out, Object value) throws IOException {

        out.writeObject(value.getClass());

        List<XFieldAccessor> fields = XReflection.of(value.getClass()).getFields()
            .filter(field -> !field.isStatic() && !field.isTransient())
            .map(field -> field.getAccessor(value))
            .collect(Collectors.toList());

        out.writeShort(fields.size());

        for (XFieldAccessor field : fields) {
            out.writeObject(field.getField().getDeclaringClass().getReflectionObject());
            out.writeObject(field.getField().getName());
            out.writeObject(field.get());
        }

    }

    @Override
    public Object create(XObjectInputStream in) throws IOException, ClassNotFoundException {
        Class clazz = in.readObject();
        return ObjenesisHelper.newInstance(clazz);
    }

    @Override
    public void readValue(XObjectInputStream in, Object value) throws IOException, ClassNotFoundException {

        int fieldCount = in.readShort();

        for (int k = 0; k < fieldCount; k++) {
            Class declaringClass = in.readObject();
            String name = in.readObject();
            Object fieldValue = in.readObject();
            XReflection.of(declaringClass).getDeclaredField(name).set(value, fieldValue);
        }

    }

}

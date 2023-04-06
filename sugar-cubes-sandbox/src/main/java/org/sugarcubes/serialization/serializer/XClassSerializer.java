package org.sugarcubes.serialization.serializer;

import java.io.IOException;

import org.sugarcubes.serialization.XObjectInputStream;
import org.sugarcubes.serialization.XObjectOutputStream;
import org.sugarcubes.serialization.XSerializer;

/**
 * todo: document it
 *
 * @author Q-MBU
 * @author Maxim Butov
 */
public class XClassSerializer implements XSerializer<Class<?>> {

    @Override
    public boolean matches(XObjectOutputStream out, Object value) {
        return value instanceof Class;
    }

    @Override
    public void writeValue(XObjectOutputStream out, Class<?> value) throws IOException {
        String className = value.getName();
        int index = className.lastIndexOf('.');
        String packageName = index > 0 ? className.substring(0, index) : "";
        String classSimpleName = className.substring(index + 1);
        out.writeObject(packageName);
        out.writeObject(classSimpleName);
    }

    @Override
    public Class<?> create(XObjectInputStream in) throws IOException, ClassNotFoundException {
        String packageName = in.readObject();
        String classSimpleName = in.readObject();
        String className = packageName.length() > 0 ? packageName + '.' + classSimpleName : classSimpleName;
        return Class.forName(className);
    }

}

package org.sugarcubes.serialization.serializer;

import java.io.IOException;

import org.sugarcubes.serialization.XObjectInputStream;
import org.sugarcubes.serialization.XObjectOutputStream;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XTrueSerializer implements XSerializer<Boolean> {

    @Override
    public boolean matches(XObjectOutputStream out, Object value) {
        return Boolean.TRUE.equals(value);
    }

    @Override
    public void writeValue(XObjectOutputStream out, Boolean value) throws IOException {
    }

    @Override
    public Boolean create(XObjectInputStream in) throws IOException, ClassNotFoundException {
        return Boolean.TRUE;
    }

}

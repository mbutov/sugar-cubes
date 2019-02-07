package org.sugarcubes.serialization.serializer;

import java.io.IOException;

import org.sugarcubes.serialization.XObjectInputStream;
import org.sugarcubes.serialization.XObjectOutputStream;

/**
 * @author Maxim Butov
 */
public class XNullSerializer implements XSerializer<Object> {

    @Override
    public int tag() {
        return 'N';
    }

    @Override
    public boolean matches(XObjectOutputStream out, Object value) {
        return value == null;
    }

    @Override
    public void writeValue(XObjectOutputStream out, Object value) throws IOException {
    }

    @Override
    public Object create(XObjectInputStream in) throws IOException, ClassNotFoundException {
        return null;
    }

}

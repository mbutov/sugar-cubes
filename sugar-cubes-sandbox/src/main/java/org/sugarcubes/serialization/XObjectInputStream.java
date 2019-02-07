package org.sugarcubes.serialization;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.sugarcubes.serialization.serializer.XSerializers;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XObjectInputStream extends DataInputStream {

    public XObjectInputStream(InputStream in) {
        super(in);
    }

    private final List<Object> objects = new ArrayList<>();

    private Object getByReference(int reference) {
        return objects.get(reference);
    }

    private int addReference() {
        int reference = objects.size();
        objects.add(null);
        return reference;
    }

    private void putReference(int reference, Object object) {
        objects.set(reference, object);
    }

    public <T> T readObject() throws IOException, ClassNotFoundException {
        int tag = readByte();
        switch (tag) {
            case XSerializers.NULL:
                return null;
            case XSerializers.REFERENCE:
                int ref = readInt();
                return (T) getByReference(ref);
            default:
                XSerializer serializer = XSerializers.SERIALIZERS.get(tag);
                if (serializer == null) {
                    throw new IllegalStateException("Serializer nof found for tag " + (char) tag);
                }
                int reference = addReference();
                Object object = serializer.create(this);
                putReference(reference, object);
                serializer.readValue(this, object);
                return (T) object;
        }
    }

}

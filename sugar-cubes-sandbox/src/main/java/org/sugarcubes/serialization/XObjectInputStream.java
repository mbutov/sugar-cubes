package org.sugarcubes.serialization;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class XObjectInputStream extends DataInputStream {

    public XObjectInputStream(InputStream in) {
        super(in);
    }

    private final List<Object> red = new ArrayList<>();

    public Object getByReference(int reference) {
        return red.get(reference);
    }

    public int addReference(Object object) {
        int reference = red.size();
        red.add(object);
        return reference;
    }

    public void putByReference(int reference, Object object) {
        red.set(reference, object);
    }

    public <T> T readObject() throws IOException, ClassNotFoundException {
        byte tag = readByte();
        XSerializer serializer = Arrays.stream(XSerializers.values())
            .filter(s -> s.tag() == tag)
            .findAny()
            .get();
        int reference = addReference(null);
        T value = (T) serializer.create(this);
        putByReference(reference, value);
        serializer.readValue(this, value);
        return value;
    }

}

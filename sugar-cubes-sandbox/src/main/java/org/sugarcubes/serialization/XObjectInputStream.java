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

    private final List<Object> red = new ArrayList<>();

    Object get(int reference) {
        return red.get(reference);
    }

    int add(Object object) {
        int reference = red.size();
        red.add(object);
        return reference;
    }

    void put(int reference, Object object) {
        red.set(reference, object);
    }

    public XObjectInputStream(InputStream in) {
        super(in);
    }

    public <T> T readObject() throws IOException, ClassNotFoundException {
        byte tag = readByte();
        XSerializer serializer = Arrays.stream(XSerializers.values())
            .filter(s -> s.tag() == tag)
            .findAny()
            .get();
        int reference = add(null);
        T value = (T) serializer.readValue(this, reference);
        put(reference, value);
        return value;
    }

}

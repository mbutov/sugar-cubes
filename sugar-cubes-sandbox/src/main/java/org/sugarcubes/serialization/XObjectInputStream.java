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

    private int nextByte() throws IOException {
        int b = read();
        if (b == -1) {
            throw new IllegalStateException("Unexpected EOF");
        }
        return b;
    }

    private char readTag() throws IOException {
        int b0 = nextByte();
        // UTF-8:   [0xxx xxxx]
        if (b0 < 0x80) {
            return (char) b0;
        }
        // UTF-8:   [110y yyyy] [10xx xxxx]
        if ((b0 & 0xE0) == 0xC0 && (b0 & 0x1E) != 0) {
            int b1 = nextByte();
            return (char) (((b0 << 6) & 0x07C0) | (b1 & 0x003F));
        }
        // UTF-8:   [1110 zzzz] [10yy yyyy] [10xx xxxx]
        if ((b0 & 0xF0) == 0xE0) {
            int b1 = nextByte();
            if ((b1 & 0xC0) != 0x80 || (b0 == 0xED && b1 >= 0xA0) || ((b0 & 0x0F) == 0 && (b1 & 0x20) == 0)) {
                throw new IllegalStateException("Unsupported character");
            }
            int b2 = nextByte();
            if ((b2 & 0xC0) != 0x80) {
                throw new IllegalStateException("Unsupported character");
            }
            return (char) (((b0 << 12) & 0xF000) | ((b1 << 6) & 0x0FC0) | (b2 & 0x003F));
        }
        throw new IllegalStateException("Unsupported character");
    }

    public <T> T readObject() throws IOException, ClassNotFoundException {
        char tag = readTag();
        switch (tag) {
            case XSerializers.NULL:
                return null;
            case XSerializers.REFERENCE:
                int ref = readInt();
                return (T) getByReference(ref);
            default:
                XSerializer serializer = XSerializers.SERIALIZERS.get(tag);
                if (serializer == null) {
                    throw new IllegalStateException("Serializer nof found for tag \\u" + Integer.toHexString(tag));
                }
                int reference = addReference();
                Object object = serializer.create(this);
                putReference(reference, object);
                serializer.readValue(this, object);
                return (T) object;
        }
    }

}

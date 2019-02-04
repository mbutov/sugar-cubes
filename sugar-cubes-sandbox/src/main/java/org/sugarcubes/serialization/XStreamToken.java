package org.sugarcubes.serialization;

import java.io.IOException;
import java.util.Arrays;

import org.sugarcubes.stream.ZeroOneCollectors;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public enum XStreamToken {

    NULL('N') {
        @Override
        void writeValue(XObjectOutputStream out, Object value) throws IOException {
        }

        @Override
        Object readValue(XObjectInputStream in) throws IOException, ClassNotFoundException {
            return null;
        }
    },

    REFERENCE('R') {
        @Override
        void writeValue(XObjectOutputStream out, Object value) throws IOException {
            out.writeInt((Integer) value);
        }

        @Override
        Object readValue(XObjectInputStream in) throws IOException, ClassNotFoundException {
            return null;
        }
    },

    STRING('S') {
        @Override
        void writeValue(XObjectOutputStream out, Object value) throws IOException {
            out.writeUTF((String) value);
        }

        @Override
        Object readValue(XObjectInputStream in) throws IOException, ClassNotFoundException {
            return in.readUTF();
        }
    },

    STRING_REFERENCE('Q') {
        @Override
        void writeValue(XObjectOutputStream out, Object value) throws IOException {
            out.writeInt((Integer) value);
        }

        @Override
        Object readValue(XObjectInputStream in) throws IOException, ClassNotFoundException {
            return in.readInt();
        }
    },

    ENUM('E') {
        @Override
        void writeValue(XObjectOutputStream out, Object value) throws IOException {
            Enum e = (Enum) value;
            out.writeString(e.getClass().getName());
            out.writeString(e.name());
        }

        @Override
        Object readValue(XObjectInputStream in) throws IOException, ClassNotFoundException {
            String className = in.readString();
            String name = in.readString();
            return Enum.valueOf((Class) Class.forName(className), name);
        }
    },

    ;

    private final int marker;

    XStreamToken(int marker) {
        this.marker = marker;
    }

    static XStreamToken findByMarker(int marker) {
        return Arrays.stream(values()).filter(token -> token.marker == marker).collect(ZeroOneCollectors.onlyElement());
    }

    void write(XObjectOutputStream out, Object value) throws IOException {
        out.writeByte(marker);
        writeValue(out, value);
    }

    abstract void writeValue(XObjectOutputStream out, Object value) throws IOException;

    Object read(XObjectInputStream in) throws IOException, ClassNotFoundException {
        int marker = in.readInt();
        XStreamToken token = findByMarker(marker);
        return token.readValue(in);
    }

    abstract Object readValue(XObjectInputStream in) throws IOException, ClassNotFoundException;

}

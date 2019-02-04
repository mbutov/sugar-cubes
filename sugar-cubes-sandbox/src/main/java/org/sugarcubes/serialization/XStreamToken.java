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
        boolean matches(Object value) {
            return value == null;
        }

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
        boolean matches(Object value) {
            return false;
        }

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
        boolean matches(Object value) {
            return value instanceof String;
        }

        @Override
        void writeValue(XObjectOutputStream out, Object value) throws IOException {
            out.writeUTF((String) value);
        }

        @Override
        Object readValue(XObjectInputStream in) throws IOException, ClassNotFoundException {
            return in.readUTF();
        }
    },

    ENUM('E') {
        @Override
        boolean matches(Object value) {
            return value instanceof Enum;
        }

        @Override
        void writeValue(XObjectOutputStream out, Object value) throws IOException {
            Enum e = (Enum) value;
            out.writeObject(e.getClass().getName());
            out.writeObject(e.name());
        }

        @Override
        Object readValue(XObjectInputStream in) throws IOException, ClassNotFoundException {
            String className = (String) in.readObject();
            String name = (String) in.readObject();
            return Enum.valueOf((Class) Class.forName(className), name);
        }
    },

    OBJECT('O') {
        @Override
        boolean matches(Object value) {
            return value instanceof Enum;
        }

        @Override
        void writeValue(XObjectOutputStream out, Object value) throws IOException {
            Enum e = (Enum) value;
            out.writeObject(e.getClass().getName());
            out.writeObject(e.name());
        }

        @Override
        Object readValue(XObjectInputStream in) throws IOException, ClassNotFoundException {
            String className = (String) in.readObject();
            String name = (String) in.readObject();
            return Enum.valueOf((Class) Class.forName(className), name);
        }
    },

    ;

    private final int marker;

    XStreamToken(int marker) {
        this.marker = marker;
    }

    abstract boolean matches(Object value);

    static XStreamToken forValue(Object value) {
        return Arrays.stream(values()).filter(token -> token.matches(value)).findFirst().get();
    }

    void write(XObjectOutputStream out, Object value) throws IOException {
        out.writeByte(marker);
        writeValue(out, value);
    }

    abstract void writeValue(XObjectOutputStream out, Object value) throws IOException;

    static XStreamToken findByMarker(int marker) {
        return Arrays.stream(values()).filter(token -> token.marker == marker).collect(ZeroOneCollectors.onlyElement());
    }

    static Object read(XObjectInputStream in) throws IOException, ClassNotFoundException {
        int marker = in.readInt();
        XStreamToken token = findByMarker(marker);
        return token.readValue(in);
    }

    abstract Object readValue(XObjectInputStream in) throws IOException, ClassNotFoundException;

}

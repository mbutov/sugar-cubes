package org.sugarcubes.serialization;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.objenesis.ObjenesisHelper;
import org.sugarcubes.reflection.XFieldAccessor;
import org.sugarcubes.reflection.XReflection;

/**
 * todo: document it
 *
 * @author Q-MBU
 */
public enum XSerializers implements XSerializer {

    NULL('N') {
        @Override
        public boolean matches(XObjectOutputStream out, Object value) {
            return value == null;
        }

        @Override
        public void writeValue(XObjectOutputStream out, Object value) throws IOException {
        }

        @Override
        public Object readValue(XObjectInputStream in, int reference) throws IOException, ClassNotFoundException {
            return null;
        }
    },

    REFERENCE('R') {
        @Override
        public boolean write(XObjectOutputStream out, Object value) throws IOException {
            int reference = out.getReference(value);
            if (reference != -1) {
                writeTag(out);
                out.writeInt(reference);
                return true;
            }
            else {
                out.putReference(value);
                return false;
            }
        }

        @Override
        public Object readValue(XObjectInputStream in, int reference) throws IOException, ClassNotFoundException {
            return in.readInt();
        }
    },

    STRING('S') {
        @Override
        public boolean matches(XObjectOutputStream out, Object value) {
            return value instanceof String;
        }

        @Override
        public void writeValue(XObjectOutputStream out, Object value) throws IOException {
            String string = (String) value;
            out.writeInt(string.length());
            new OutputStreamWriter(out, StandardCharsets.UTF_8).write(string);
        }

        @Override
        public Object readValue(XObjectInputStream in, int reference) throws IOException, ClassNotFoundException {
            int length = in.readInt();
            char[] cbuf = new char[length];
            int count = new InputStreamReader(in, StandardCharsets.UTF_8).read(cbuf);
            if (count != length) {
                throw new IOException("Cannot fully read string");
            }
            return in.readUTF();
        }
    },

    CLASS('C') {
        @Override
        public boolean matches(XObjectOutputStream out, Object value) {
            return value instanceof Class;
        }

        @Override
        public void writeValue(XObjectOutputStream out, Object value) throws IOException {
            out.writeObject(((Class) value).getName());
        }

        @Override
        public Object readValue(XObjectInputStream in, int reference) throws IOException, ClassNotFoundException {
            return Class.forName(in.readObject());
        }
    },

    ENUM('E') {
        @Override
        public boolean matches(XObjectOutputStream out, Object value) {
            return value instanceof Enum;
        }

        @Override
        public void writeValue(XObjectOutputStream out, Object value) throws IOException {
            Enum e = (Enum) value;
            out.writeObject(e.getClass());
            out.writeObject(e.name());
        }

        @Override
        public Object readValue(XObjectInputStream in, int reference) throws IOException, ClassNotFoundException {
            Class enumType = in.readObject();
            String name = in.readObject();
            return Enum.valueOf(enumType, name);
        }
    },

    INTEGER('I') {
        @Override
        public boolean matches(XObjectOutputStream out, Object value) {
            return value instanceof Integer;
        }

        @Override
        public void writeValue(XObjectOutputStream out, Object value) throws IOException {
            out.writeInt((Integer) value);
        }

        @Override
        public Object readValue(XObjectInputStream in, int reference) throws IOException, ClassNotFoundException {
            return in.readInt();
        }
    },

    OBJECT('O') {
        @Override
        public boolean matches(XObjectOutputStream out, Object value) {
            return true;
        }

        @Override
        public void writeValue(XObjectOutputStream out, Object value) throws IOException {
            out.writeObject(value.getClass());
            List<XFieldAccessor<?>> accessors = XReflection.of(value.getClass()).getFields()
                .map(field -> field.getAccessor(value))
                .collect(Collectors.toList());
            out.writeShort(accessors.size());
            for (XFieldAccessor<?> accessor : accessors) {
                out.writeObject(accessor.getField().getDeclaringClass().getReflectionObject());
                out.writeObject(accessor.getField().getName());
                out.writeObject(accessor.get());
            }
        }

        @Override
        public Object readValue(XObjectInputStream in, int reference) throws IOException, ClassNotFoundException {

            Class type = in.readObject();

            Object object = ObjenesisHelper.newInstance(type);

            in.put(reference, object);

            int count = in.readShort();
            for (int k = 0; k < count; k++) {
                Class<?> declaringClass = in.readObject();
                String name = in.readObject();
                Object value = in.readObject();
                XReflection.of(declaringClass).getField(name).getAccessor(object).set(value);
            }

            return object;
        }
    },

    ERROR(-1) {
        @Override
        public boolean write(XObjectOutputStream out, Object value) throws IOException {
            throw new IllegalStateException();
        }

        @Override
        public Object readValue(XObjectInputStream in, int reference) throws IOException, ClassNotFoundException {
            throw new IllegalStateException();
        }
    };

    private final int tag;

    XSerializers(int tag) {
        this.tag = tag;
    }

    @Override
    public int tag() {
        return 0;
    }

}

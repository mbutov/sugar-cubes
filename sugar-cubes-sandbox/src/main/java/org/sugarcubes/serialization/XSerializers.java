package org.sugarcubes.serialization;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
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
            return in.get(in.readInt());
        }
    },

    STRING('S') {

        final CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        final CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();

        @Override
        public boolean matches(XObjectOutputStream out, Object value) {
            return value instanceof String;
        }

        @Override
        public void writeValue(XObjectOutputStream out, Object value) throws IOException {
            String string = (String) value;
            ByteBuffer buffer = encoder.encode(CharBuffer.wrap(string));
            byte[] bytes = buffer.array();
            out.writeInt(bytes.length);
            out.write(bytes);
        }

        @Override
        public Object readValue(XObjectInputStream in, int reference) throws IOException, ClassNotFoundException {
            int length = in.readInt();
            byte[] bytes = new byte[length];
            int count = in.read(bytes);
            if (count != length) {
                throw new IOException("Cannot fully read string");
            }
            return decoder.decode(ByteBuffer.wrap(bytes)).toString();
        }
    },

    CLASS('C') {
        @Override
        public boolean matches(XObjectOutputStream out, Object value) {
            return value instanceof Class;
        }

        @Override
        public void writeValue(XObjectOutputStream out, Object value) throws IOException {
            Class clazz = (Class) value;
            String className = clazz.getName();
            int index = className.lastIndexOf('.');
            if (index >= 0) {
                out.writeObject(className.substring(0, index));
                out.writeObject(className.substring(index + 1));
            }
            else {
                out.writeObject("");
                out.writeObject(className);
            }
        }

        @Override
        public Object readValue(XObjectInputStream in, int reference) throws IOException, ClassNotFoundException {
            String packageName = in.readObject();
            String classSimpleName = in.readObject();
            String className = packageName.length() > 0 ? packageName + '.' + classSimpleName : classSimpleName;
            return Class.forName(className);
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
        return tag;
    }

}

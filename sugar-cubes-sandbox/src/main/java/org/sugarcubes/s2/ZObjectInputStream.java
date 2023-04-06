package org.sugarcubes.s2;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectStreamConstants;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import java.util.Set;
import java.util.function.Consumer;

public class ZObjectInputStream extends ZDataInputStream implements ObjectInput {

    public ZObjectInputStream(InputStream input) throws IOException {
        super(input);
        readStreamHeader();
    }

    /**
     * The readStreamHeader method is provided to allow subclasses to read and
     * verify their own stream headers. It reads and verifies the magic number
     * and version number.
     *
     * @throws IOException if there are I/O errors while reading from the
     * underlying <code>InputStream</code>
     * @throws StreamCorruptedException if control information in the stream
     * is inconsistent
     */
    protected void readStreamHeader() throws IOException {
        for (short s0 = readShort(); s0 != ObjectStreamConstants.STREAM_MAGIC; ) {
            throw ZExceptions.invalid("stream start", s0);
        }
        for (short s1 = readShort(); s1 != ObjectStreamConstants.STREAM_VERSION; ) {
            throw ZExceptions.invalid("stream version", s1);
        }
    }

    private final ZHandles handles = new ZHandles();

    @Override
    public Object readObject() throws ClassNotFoundException, IOException {
        return readNext(Object.class, ZTag.ANY_OBJECT_RESET_TAGS);
    }

    private ZTag readTag(Set<ZTag> allowed) throws IOException {
        ZTag tag = ZTag.valueOf(readByte());
        if (!allowed.contains(tag)) {
            throw new IOException(String.format("Tag %s is not allowed, expected one of %s", tag, allowed));
        }
        return tag;
    }

    protected <T> T readNext(Class<T> type, Set<ZTag> allowed) throws IOException, ClassNotFoundException {
        ZTag tag;

        while ((tag = readTag(allowed)) == ZTag.RESET) {
            handles.clear();
        }

        switch (tag) {
            case NULL:
                return null;
            case REFERENCE:
                return type.cast(handles.get(readInt() - ObjectStreamConstants.baseWireHandle));
            case STRING:
                String shortString = readShortString();
                handles.add(shortString);
                return type.cast(shortString);
            case LONGSTRING:
                String longString = readLongString();
                handles.add(longString);
                return type.cast(longString);
            case OBJECT:
                return readObject(type);
            case CLASSDESC:
                return readClassDescriptor(type);
            case PROXYCLASSDESC:
                return readProxyDescriptor(type);
            case ARRAY:
                return readArray(type);
            case ENUM:
                return readEnum(type);
            case CLASS:
                return readClass(type);
            default:
                throw ZExceptions.invalid("tag", tag);
        }
    }

    private class BlockDataInputStream extends InputStream {

        private int available;

        private final byte[] buffer = new byte[16];
        private int pos;
        private int count;

        @Override
        public int read() throws IOException {
            if (available < 0) {
                return -1;
            }
            if (pos == count) {
                while (available == 0) {
                    ZTag tag = readTag(ZTag.ANY_TAGS);
                    switch (tag) {
                        case BLOCKDATA:
                            available = readUnsignedByte();
                            break;
                        case BLOCKDATALONG:
                            available = readInt();
                            break;
                        default:
                            unread(tag.getTag());
                        case ENDBLOCKDATA:
                            available = -1;
                            return -1;
                    }
                }
                pos = 0;
                count = Math.min(available, buffer.length);
                readFully(buffer, pos, count);
                available -= count;
            }
            return buffer[pos++] & 0xFF;
        }

    }

    protected InputStream blockDataInputStream() {
        return new BlockDataInputStream();
    }

    private <T> T readClassDescriptor(Class<T> type) throws IOException, ClassNotFoundException {

        String name = readUTF();
        long serialVersionUid = readLong();
        Set<ZTypeFlag> flags = ZTypeFlag.valueOf(readByte());
        int numFields = readShort();

        ZClassDescriptor ocd = new ZClassDescriptor(name, serialVersionUid, flags, numFields);
        handles.add(ocd);

        ocd.readDescriptor(this);

        return type.cast(ocd);
    }

    private <T> T readProxyDescriptor(Class<T> type) throws IOException, ClassNotFoundException {

        int numInterfaces = readInt();
        String[] interfaces = new String[numInterfaces];
        for (int k = 0; k < numInterfaces; k++) {
            interfaces[k] = readUTF();
        }

        ZProxyDescriptor pcd = new ZProxyDescriptor(interfaces);
        handles.add(pcd);

        pcd.readDescriptor(this);

        return type.cast(pcd);
    }

    private <T> T readClass(Class<T> type) throws IOException, ClassNotFoundException {
        ZDescriptor classDescriptor = readNext(ZDescriptor.class, ZTag.NOT_NULL_CLASSDESC_TAGS);
        Class<?> result = classDescriptor.getType();
        handles.add(result);
        return type.cast(result);
    }

    private <T> T readArray(Class<T> type) throws IOException, ClassNotFoundException {
        ZClassDescriptor arrayClassDescriptor = readNext(ZClassDescriptor.class, ZTag.NOT_NULL_CLASSDESC_TAGS);
        int length = readInt();
        Class<?> componentType = arrayClassDescriptor.getType().getComponentType();
        if (componentType.isPrimitive()) {
            ZPrimitive zp = ZPrimitive.valueOf(componentType);
            Object array = zp.readArray(length, this);
            handles.add(array);
            return type.cast(array);
        }
        else {
            Object[] array = (Object[]) Array.newInstance(componentType, length);
            handles.add(array);
            for (int k = 0; k < length; k++) {
                array[k] = readNext(componentType, ZTag.ANY_OBJECT_TAGS);
            }
            return type.cast(array);
        }
    }

    private <T> T readEnum(Class<T> type) throws IOException, ClassNotFoundException {
        ZClassDescriptor ecd = readNext(ZClassDescriptor.class, ZTag.NOT_NULL_CLASSDESC_TAGS);
        Consumer<Object> handle = handles.add();
        String enumConstantName = readNext(String.class, ZTag.NOT_NULL_STRING_TAGS);
        Object obj = Enum.valueOf((Class) ecd.getType(), enumConstantName);
        handle.accept(obj);
        return type.cast(obj);
    }

    private <T> T readObject(Class<T> type) throws IOException, ClassNotFoundException {
        ZDescriptor descriptor = readNext(ZDescriptor.class, ZTag.NOT_NULL_CLASSDESC_TAGS);
        Object result = descriptor.newInstance();
        Consumer<Object> handle = handles.add(result);
        descriptor.readObject(this, result);
        result = descriptor.resolve(result);
        handle.accept(result);
        return type.cast(result);
    }

    private String readShortString() throws IOException {
        return readUTF();
    }

    private String readLongString() throws IOException {
        return readUTF(readLong());
    }

}

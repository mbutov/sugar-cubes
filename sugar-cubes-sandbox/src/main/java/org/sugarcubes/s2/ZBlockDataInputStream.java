package org.sugarcubes.s2;

import java.io.IOException;
import java.io.ObjectStreamConstants;
import java.util.function.BiFunction;

public class ZBlockDataInputStream extends ZDataInputStream {

    private final ZDataInputStream input;

    public ZBlockDataInputStream(ZDataInputStream input) {
        super(input);
        this.input = input;
    }

    @Override
    public void unread(int b) throws IOException {
        input.unread(b);
    }

    @Override
    public void unread(byte[] b, int off, int len) throws IOException {
        input.unread(b, off, len);
    }

    @Override
    public void unread(byte[] b) throws IOException {
        input.unread(b, 0, b.length);
    }

    private int available;

    private boolean hasMoreData() throws IOException {
        if (available < 0) {
            return false;
        }
        while (available == 0) {
            int tag = input.read();
            switch (tag) {
                case ObjectStreamConstants.TC_BLOCKDATA:
                    available = input.readUnsignedByte();
                    break;
                case ObjectStreamConstants.TC_BLOCKDATALONG:
                    available = input.readInt();
                    break;
                default:
                    input.unread(tag);
                case -1:
                case ObjectStreamConstants.TC_ENDBLOCKDATA:
                    available = -1;
                    return false;
            }
        }
        return true;
    }

    @Override
    public int available() throws IOException {
        return hasMoreData() ? Math.min(available, input.available()) : 0;
    }

    private int available(long count) {
        return (int) Math.min(available, count);
    }

    @Override
    public int read() throws IOException {
        if (hasMoreData()) {
            available--;
            return input.read();
        }
        else {
            return -1;
        }
    }

    @Override
    public int read(byte[] bytes, int off, int len) throws IOException {
        int count = 0;
        while (count < len && hasMoreData()) {
            int next = input.read(bytes, off + count, available(len - count));
            if (next == 0) {
                break;
            }
            count += next;
            available -= next;
        }
        return count;
    }

    @Override
    public long skip(long len) throws IOException {
        long count = 0;
        while (count < len && hasMoreData()) {
            int next = (int) input.skip(available(len - count));
            if (next == 0) {
                break;
            }
            count += next;
            available -= next;
        }
        return count;
    }

}

package org.sugarcubes.s2;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ZLimitedInputStream extends FilterInputStream {

    private final boolean suppressClose;

    private long limit;
    private long mark;

    public ZLimitedInputStream(InputStream in, boolean suppressClose, long limit) {
        super(in);
        this.suppressClose = suppressClose;
        this.limit = limit;
    }

    public ZLimitedInputStream(InputStream in, long limit) {
        this(in, false, limit);
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    @Override
    public int read() throws IOException {
        if (limit == 0) {
            return -1;
        }
        int b = super.read();
        if (b >= 0) {
            limit--;
        }
        return b;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (limit == 0) {
            return -1;
        }
        int count = super.read(b, off, (int) Math.min(len, limit));
        if (count > 0) {
            limit -= count;
        }
        return count;
    }

    @Override
    public long skip(long n) throws IOException {
        if (limit <= 0) {
            return 0;
        }
        long skip = super.skip(Math.min(n, limit));
        limit -= skip;
        return skip;
    }

    @Override
    public int available() throws IOException {
        return (int) Math.min(super.available(), limit);
    }

    @Override
    public synchronized void mark(int readlimit) {
        super.mark(readlimit);
        mark = limit;
    }

    @Override
    public synchronized void reset() throws IOException {
        super.reset();
        limit = mark;
    }

    @Override
    public void close() throws IOException {
        if (!suppressClose) {
            super.close();
        }
    }
}

package org.sugarcubes.s2;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

public class YObjectInputStream extends ObjectInputStream {

    private final ZDataInputStream rawInput;
    private final ZBlockDataInputStream blockInput;

    private boolean blockMode = false;

    private ZDataInputStream input() {
        return blockMode ? blockInput : rawInput;
    }

    public YObjectInputStream(InputStream in) throws IOException {
        super();
        rawInput = new ZObjectInputStream(in);
        blockInput = new ZBlockDataInputStream(rawInput);
    }

    @Override
    protected Object readObjectOverride() throws IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public int read() throws IOException {
        return rawInput.read();
    }

    @Override
    public int read(byte[] buf, int off, int len) throws IOException {
        return rawInput.read(buf, off, len);
    }

    @Override
    public int available() throws IOException {
        return rawInput.available();
    }

    @Override
    public void close() throws IOException {
        rawInput.close();
    }

    @Override
    public boolean readBoolean() throws IOException {
        return rawInput.readBoolean();
    }

    @Override
    public byte readByte() throws IOException {
        return rawInput.readByte();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return rawInput.readUnsignedByte();
    }

    @Override
    public char readChar() throws IOException {
        return rawInput.readChar();
    }

    @Override
    public short readShort() throws IOException {
        return rawInput.readShort();
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return rawInput.readUnsignedShort();
    }

    @Override
    public int readInt() throws IOException {
        return rawInput.readInt();
    }

    @Override
    public long readLong() throws IOException {
        return rawInput.readLong();
    }

    @Override
    public float readFloat() throws IOException {
        return rawInput.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        return rawInput.readDouble();
    }

    @Override
    public void readFully(byte[] buf) throws IOException {
        rawInput.readFully(buf);
    }

    @Override
    public void readFully(byte[] buf, int off, int len) throws IOException {
        rawInput.readFully(buf, off, len);
    }

    @Override
    public int skipBytes(int len) throws IOException {
        return rawInput.skipBytes(len);
    }

    @Override
    public String readLine() throws IOException {
        return rawInput.readLine();
    }

    @Override
    public String readUTF() throws IOException {
        return rawInput.readUTF();
    }

}

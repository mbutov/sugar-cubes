package sun.misc;

import java.lang.reflect.Field;

/**
 * A stub for sun.misc.Unsafe.
 *
 * @author Maxim Butov
 */
public interface Unsafe {

    int getInt(Object o, long offset);

    void putInt(Object o, long offset, int x);

    Object getObject(Object o, long offset);

    void putObject(Object o, long offset, Object x);

    boolean getBoolean(Object o, long offset);

    void putBoolean(Object o, long offset, boolean x);

    byte getByte(Object o, long offset);

    void putByte(Object o, long offset, byte x);

    short getShort(Object o, long offset);

    void putShort(Object o, long offset, short x);

    char getChar(Object o, long offset);

    void putChar(Object o, long offset, char x);

    long getLong(Object o, long offset);

    void putLong(Object o, long offset, long x);

    float getFloat(Object o, long offset);

    void putFloat(Object o, long offset, float x);

    double getDouble(Object o, long offset);

    void putDouble(Object o, long offset, double x);

    byte getByte(long address);

    void putByte(long address, byte x);

    short getShort(long address);

    void putShort(long address, short x);

    char getChar(long address);

    void putChar(long address, char x);

    int getInt(long address);

    void putInt(long address, int x);

    long getLong(long address);

    void putLong(long address, long x);

    float getFloat(long address);

    void putFloat(long address, float x);

    double getDouble(long address);

    void putDouble(long address, double x);

    long objectFieldOffset(Field f);

    Object allocateInstance(Class<?> cls) throws InstantiationException;

}

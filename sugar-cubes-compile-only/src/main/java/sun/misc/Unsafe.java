package sun.misc;

import java.lang.reflect.Field;

/**
 * A stub for sun.misc.Unsafe (compile only).
 *
 * @author Maxim Butov
 */
public abstract class Unsafe {

    public abstract Object allocateInstance(Class<?> cls) throws InstantiationException;

    public abstract long objectFieldOffset(Field f);

    public abstract int getInt(Object o, long offset);

    public abstract void putInt(Object o, long offset, int x);

    public abstract Object getObject(Object o, long offset);

    public abstract void putObject(Object o, long offset, Object x);

    public abstract boolean getBoolean(Object o, long offset);

    public abstract void putBoolean(Object o, long offset, boolean x);

    public abstract byte getByte(Object o, long offset);

    public abstract void putByte(Object o, long offset, byte x);

    public abstract short getShort(Object o, long offset);

    public abstract void putShort(Object o, long offset, short x);

    public abstract char getChar(Object o, long offset);

    public abstract void putChar(Object o, long offset, char x);

    public abstract long getLong(Object o, long offset);

    public abstract void putLong(Object o, long offset, long x);

    public abstract float getFloat(Object o, long offset);

    public abstract void putFloat(Object o, long offset, float x);

    public abstract double getDouble(Object o, long offset);

    public abstract void putDouble(Object o, long offset, double x);

}

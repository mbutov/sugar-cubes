package org.sugarcubes.s2;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZSerialInfo {

    private static final Map<Class<?>, ZSerialInfo> INFO = new ConcurrentHashMap<>();

    public static ZSerialInfo of(Class<?> type) {
        if (type == null || !Serializable.class.isAssignableFrom(type) || Proxy.isProxyClass(type)) {
            return new ZSerialInfo(type, true);
        }
        ZSerialInfo info = INFO.get(type);
        return info != null ? info : INFO.computeIfAbsent(type, ZSerialInfo::new);
    }

    private final Class<?> type;

    private final Method writeObject;
    private final Method readObject;
    private final Method readObjectNoData;
    private final Method writeReplace;
    private final Method readResolve;

    private final long serialVersionUID;

    private ZSerialInfo(Class<?> type) {
        this.type = type;

        this.writeObject = findMethod(true, void.class, "writeObject", ObjectOutputStream.class);
        this.readObject = findMethod(true, void.class, "readObject", ObjectInputStream.class);
        this.readObjectNoData = findMethod(true, void.class, "readObjectNoData");
        this.writeReplace = findMethod(false, Object.class, "writeReplace");
        this.readResolve = findMethod(false, Object.class, "readResolve");

        this.serialVersionUID = findSerialVersionUid();
    }

    private ZSerialInfo(Class<?> type, boolean typeOnly) {
        this.type = type;

        this.writeObject = null;
        this.readObject = null;
        this.readObjectNoData = null;
        this.writeReplace = null;
        this.readResolve = null;

        this.serialVersionUID = 0L;
    }

    private Method findMethod(boolean mustBePrivate, Class<?> returnType, String name, Class<?>... parameterTypes) {
        if (type == null) {
            return null;
        }
        Method method;
        try {
            method = type.getDeclaredMethod(name, parameterTypes);
        }
        catch (NoSuchMethodException e) {
            return null;
        }
        if (returnType != method.getReturnType()) {
            return null;
        }
        int modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers)) {
            return null;
        }
        if (mustBePrivate && !Modifier.isPrivate(modifiers)) {
            return null;
        }
        method.setAccessible(true);
        return method;
    }

    public ZSerialInfo getSuper() {
        return type == null ? this : of(type.getSuperclass());
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean hasWriteObject() {
        return writeObject != null;
    }

    public boolean hasReadObject() {
        return readObject != null;
    }

    public boolean hasReadObjectNoData() {
        return readObjectNoData != null;
    }

    public boolean hasWriteReplace() {
        return writeReplace != null;
    }

    public boolean hasReadResolve() {
        return readResolve != null;
    }

    public void writeObject(Object obj, ObjectOutputStream out) throws IOException {
        invoke(writeObject, obj, out);
    }

    public void readObject(Object obj, ObjectInputStream in) throws IOException, ClassNotFoundException {
        invoke(readObject, obj, in);
    }

    public void readObjectNoData(Object obj) throws ObjectStreamException {
        invoke(readObjectNoData, obj);
    }

    public Object writeReplace(Object obj) throws ObjectStreamException {
        return invoke(writeReplace, obj);
    }

    public Object readResolve(Object obj) throws ObjectStreamException {
        return invoke(readResolve, obj);
    }

    private Object invoke(Method method, Object obj, Object... params) {
        try {
            return method.invoke(obj, params);
        }
        catch (IllegalAccessException e) {
            throw rethrow(new IOException(e));
        }
        catch (InvocationTargetException e) {
            throw rethrow(e.getTargetException());
        }
    }

    private <E extends Throwable> E rethrow(Throwable ex) throws E {
        throw (E) ex;
    }

    private long findSerialVersionUid() {
        Field field;
        try {
            field = type.getDeclaredField("serialVersionUID");
        }
        catch (NoSuchFieldException e) {
            return defaultSerialVersionUid();
        }
        int modifiers = field.getModifiers();
        if (!Modifier.isStatic(modifiers) || !Modifier.isFinal(modifiers)) {
            return defaultSerialVersionUid();
        }
        if (field.getType() != long.class) {
            return defaultSerialVersionUid();
        }
        field.setAccessible(true);
        try {
            return (Long) field.get(null);
        }
        catch (IllegalAccessException e) {
            throw rethrow(new IOException(e));
        }
    }

    private long defaultSerialVersionUid() {
        return ObjectStreamClass.lookup(type).getSerialVersionUID();
    }

}

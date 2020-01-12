package org.sugarcubes.thread;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import org.sugarcubes.reflection.XClass;
import org.sugarcubes.reflection.XField;
import org.sugarcubes.reflection.XMethod;
import org.sugarcubes.reflection.XReflection;

/**
 * @author Maxim Butov
 */
public class ThreadLocals {

    public static Set<ThreadLocal<?>> getThreadLocals(Thread thread, ThreadLocal<?> threadLocal) {
        Object map = TL_GET_MAP_METHOD.invoke(threadLocal, thread);
        if (map != null) {
            WeakReference<ThreadLocal<?>>[] table = TLM_TABLE_FIELD.get(map);
            return Arrays.stream(table)
                .filter(Objects::nonNull)
                .map(Reference::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> Collections.newSetFromMap(new WeakHashMap<>())));
        }
        else {
            return Collections.emptySet();
        }
    }

    public static <X> X getValue(Thread thread, ThreadLocal<X> threadLocal) {
        Object map = TL_GET_MAP_METHOD.invoke(threadLocal, thread);
        if (map != null) {
            WeakReference<ThreadLocal<?>> entry = TLM_GET_ENTRY_METHOD.invoke(map, threadLocal);
            if (entry != null) {
                return (X) TLME_VALUE_FIELD.get(entry);
            }
        }
        return null;
    }

    public static <X> X setValue(Thread thread, ThreadLocal<X> threadLocal, X newValue) {
        Object map = TL_GET_MAP_METHOD.invoke(threadLocal, thread);
        if (map == null) {
            TL_CREATE_MAP_METHOD.invoke(threadLocal, thread);
            map = TL_GET_MAP_METHOD.invoke(threadLocal, thread);
        }
        WeakReference<ThreadLocal<?>> entry = TLM_GET_ENTRY_METHOD.invoke(map, threadLocal);
        if (entry != null) {
            return (X) TLME_VALUE_FIELD.put(entry, newValue);
        }
        else {
            TLM_SET_METHOD.invoke(map, threadLocal, newValue);
            return null;
        }
    }

    public static void remove(Thread thread, ThreadLocal<?> threadLocal) {
        Object map = TL_GET_MAP_METHOD.invoke(threadLocal, thread);
        if (map != null) {
            TLM_REMOVE_METHOD.invoke(map, threadLocal);
        }
    }

    static final XClass<ThreadLocal<?>> TL_XCLASS = XReflection.forName("java.lang.ThreadLocal");

    static final XMethod<Object> TL_GET_MAP_METHOD = TL_XCLASS.getMethod("getMap", Thread.class);
    static final XMethod<Object> TL_CREATE_MAP_METHOD = TL_XCLASS.getMethod("createMap", Thread.class, Object.class);

    static final XClass<Object> TLM_XCLASS = XReflection.forName("java.lang.ThreadLocal$ThreadLocalMap");

    static final XField<WeakReference<ThreadLocal<?>>[]> TLM_TABLE_FIELD = TLM_XCLASS.getField("table");
    static final XMethod<WeakReference<ThreadLocal<?>>> TLM_GET_ENTRY_METHOD = TLM_XCLASS.getMethod("getEntry", ThreadLocal.class);
    static final XMethod<Void> TLM_SET_METHOD = TLM_XCLASS.getMethod("set", ThreadLocal.class, Object.class);
    static final XMethod<Void> TLM_REMOVE_METHOD = TLM_XCLASS.getMethod("remove", ThreadLocal.class);

    static final XClass<WeakReference<ThreadLocal<?>>> TLME_XCLASS = XReflection.forName("java.lang.ThreadLocal$ThreadLocalMap$Entry");
    static final Object[] TLME_EMPTY_ARRAY = (Object[]) Array.newInstance(TLME_XCLASS.getReflectionObject(), 0);
    static final XField<Object> TLME_VALUE_FIELD = TLME_XCLASS.getField("value");

}

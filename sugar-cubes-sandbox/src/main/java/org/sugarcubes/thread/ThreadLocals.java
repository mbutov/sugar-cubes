package org.sugarcubes.thread;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.stream.Stream;

import org.sugarcubes.reflection.XClass;
import org.sugarcubes.reflection.XField;
import org.sugarcubes.reflection.XMethod;
import org.sugarcubes.reflection.XReflection;

/**
 * @author Maxim Butov
 */
public class ThreadLocals {

    public static Map<ThreadLocal<?>, Object> getAll(Thread thread) {
        Map<ThreadLocal<?>, Object> threadLocals = new WeakHashMap<>();
        threadLocalTypes().forEach(threadLocal -> threadLocals.putAll(getAll(thread, threadLocal)));
        return threadLocals;
    }

    public static Map<ThreadLocal<?>, Object> getAll(Thread thread, ThreadLocal<?> threadLocal) {
        Object map = TL_GET_MAP_METHOD.invoke(threadLocal, thread);
        if (map != null) {
            Map<ThreadLocal<?>, Object> threadLocals = new WeakHashMap<>();
            Arrays.stream(TLM_TABLE_FIELD.get(map))
                .filter(Objects::nonNull)
                .forEach(entry -> {
                    ThreadLocal<?> key = entry.get();
                    if (key != null) {
                        threadLocals.put(key, TLME_VALUE_FIELD.get(entry));
                    }
                });
            return threadLocals;
        }
        else {
            return Collections.emptyMap();
        }
    }

    public static void clearAll(Thread thread) {
        threadLocalTypes().forEach(threadLocal -> clearAll(thread, threadLocal));
    }

    public static void clearAll(Thread thread, ThreadLocal<?> threadLocal) {
        Object map = TL_GET_MAP_METHOD.invoke(threadLocal, thread);
        if (map != null) {
            TL_CREATE_MAP_METHOD.invoke(threadLocal, thread, null);
            map = TL_GET_MAP_METHOD.invoke(threadLocal, thread);
            TLM_REMOVE_METHOD.invoke(map, threadLocal);
        }
    }

    public static <X> X get(Thread thread, ThreadLocal<X> threadLocal) {
        Object map = TL_GET_MAP_METHOD.invoke(threadLocal, thread);
        if (map != null) {
            WeakReference<ThreadLocal<?>> entry = TLM_GET_ENTRY_METHOD.invoke(map, threadLocal);
            if (entry != null) {
                return (X) TLME_VALUE_FIELD.get(entry);
            }
        }
        return null;
    }

    public static <X> X set(Thread thread, ThreadLocal<X> threadLocal, X newValue) {
        Object map = TL_GET_MAP_METHOD.invoke(threadLocal, thread);
        if (map == null) {
            TL_CREATE_MAP_METHOD.invoke(threadLocal, thread, newValue);
            return null;
        }
        WeakReference<ThreadLocal<?>> entry = TLM_GET_ENTRY_METHOD.invoke(map, threadLocal);
        if (entry != null) {
            return (X) TLME_VALUE_FIELD.getAndSet(entry, newValue);
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

    private static Stream<ThreadLocal<?>> threadLocalTypes() {
        return Stream.of(new ThreadLocal<>(), new InheritableThreadLocal<>());
    }

    private static final XClass<ThreadLocal<?>> TL_XCLASS = XReflection.forName("java.lang.ThreadLocal");
    private static final XMethod<Object> TL_GET_MAP_METHOD = TL_XCLASS.getMethod("getMap", Thread.class);
    private static final XMethod<Object> TL_CREATE_MAP_METHOD = TL_XCLASS.getMethod("createMap", Thread.class, Object.class);

    private static final XClass<Object> TLM_XCLASS = XReflection.forName("java.lang.ThreadLocal$ThreadLocalMap");
    private static final XField<WeakReference<ThreadLocal<?>>[]> TLM_TABLE_FIELD = TLM_XCLASS.getField("table");
    private static final XMethod<WeakReference<ThreadLocal<?>>> TLM_GET_ENTRY_METHOD =
        TLM_XCLASS.getMethod("getEntry", ThreadLocal.class);
    private static final XMethod<Void> TLM_SET_METHOD = TLM_XCLASS.getMethod("set", ThreadLocal.class, Object.class);
    private static final XMethod<Void> TLM_REMOVE_METHOD = TLM_XCLASS.getMethod("remove", ThreadLocal.class);

    private static final XClass<WeakReference<ThreadLocal<?>>> TLME_XCLASS =
        XReflection.forName("java.lang.ThreadLocal$ThreadLocalMap$Entry");
    private static final XField<Object> TLME_VALUE_FIELD = TLME_XCLASS.getField("value");

}

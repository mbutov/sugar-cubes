package org.sugarcubes.thread;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * todo: document it
 *
 * @author Q-MBU
 */
public class ThreadLocalsMap extends AbstractMap<ThreadLocal<?>, Object> {

    private final Thread thread;
    private final ThreadLocal<?> threadLocal;

    public ThreadLocalsMap(ThreadLocal<?> threadLocal) {
        this(Thread.currentThread(), threadLocal);
    }

    public ThreadLocalsMap(Thread thread, ThreadLocal<?> threadLocal) {
        this.thread = Objects.requireNonNull(thread, "thread");
        this.threadLocal = Objects.requireNonNull(threadLocal, "threadLocal");
    }

    @Override
    public int size() {
        return (int) stream().count();
    }

    @Override
    public Object get(Object key) {
        if (key instanceof ThreadLocal) {
            Object map = map();
            if (map != null) {
                Object entry = Reflection.invoke(map, TLM_GET_ENTRY_METHOD, key);
                if (entry != null) {
                    return Reflection.get(entry, TLME_VALUE_FIELD);
                }
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return key != null && Arrays.asList(table()).contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return stream().anyMatch(entry -> Objects.equals(Reflection.get(entry, TLME_VALUE_FIELD), value));
    }

    @Override
    public Object put(ThreadLocal<?> key, Object value) {
        Object previous = null;
        Object map = map();
        if (map == null) {
            Reflection.invoke(key, TL_CREATE_MAP_METHOD, thread, value);
        }
        else {
            Object entry = Reflection.invoke(map, TLM_GET_ENTRY_METHOD, key);
            if (entry != null) {
                previous = Reflection.get(entry, TLME_VALUE_FIELD);
            }
            Reflection.invoke(map, TLM_SET_METHOD, key, value);
        }
        return previous;
    }

    @Override
    public Object remove(Object key) {
        if (key instanceof ThreadLocal) {
            Object map = map();
            if (map != null) {
                Object entry = Reflection.invoke(map, TLM_GET_ENTRY_METHOD, key);
                if (entry != null) {
                    Reflection.invoke(map, TLM_REMOVE_METHOD, key);
                    return Reflection.get(entry, TLME_VALUE_FIELD);
                }
            }
        }
        return null;
    }

    @Override
    public void clear() {
        Reflection.invoke(threadLocal, TL_CREATE_MAP_METHOD, thread, null);
        Reflection.invoke(map(), TLM_REMOVE_METHOD, threadLocal);
    }

    @Override
    public Set<Entry<ThreadLocal<?>, Object>> entrySet() {
        return new EntrySet();
    }

    // private

    class EntrySet extends AbstractSet<Entry<ThreadLocal<?>, Object>> {

        @Override
        public boolean contains(Object o) {
            if (o instanceof Entry) {
                Entry e = (Entry) o;
                Object key = e.getKey();
                if (key instanceof ThreadLocal) {
                    return Objects.equals(ThreadLocalsMap.this.get(key), e.getValue());
                }
            }
            return false;
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Entry) {
                Entry e = (Entry) o;
                Object key = e.getKey();
                if (key instanceof ThreadLocal) {
                    if (Objects.equals(ThreadLocalsMap.this.get(key), e.getValue())) {
                        ThreadLocalsMap.this.remove(key);
                    }
                }
            }
            return false;
        }

        @Override
        public void clear() {
            ThreadLocalsMap.this.clear();
        }

        @Override
        public Iterator<Entry<ThreadLocal<?>, Object>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return ThreadLocalsMap.this.size();
        }

    }

    class EntryIterator implements Iterator<Entry<ThreadLocal<?>, Object>> {

        Iterator<? extends ThreadLocal<?>> iterator = stream().map(Reference::get).filter(Objects::nonNull).iterator();
        ThreadLocal<?> last;

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Entry<ThreadLocal<?>, Object> next() {
            last = iterator.next();
            return new ThreadLocalEntry(last);
        }

        @Override
        public void remove() {
            if (last == null) {
                throw new IllegalStateException("next() was not called");
            }
            ThreadLocalsMap.this.remove(last);
        }

    }

    class ThreadLocalEntry implements Entry<ThreadLocal<?>, Object> {

        final ThreadLocal<?> key;

        public ThreadLocalEntry(ThreadLocal<?> key) {
            this.key = key;
        }

        @Override
        public ThreadLocal<?> getKey() {
            return key;
        }

        @Override
        public Object getValue() {
            return ThreadLocalsMap.this.get(key);
        }

        @Override
        public Object setValue(Object value) {
            return ThreadLocalsMap.this.put(key, value);
        }

    }

    Object map() {
        return Reflection.invoke(threadLocal, TL_GET_MAP_METHOD, thread);
    }

    Reference<ThreadLocal<?>>[] table() {
        Object map = map();
        return map != null ? Reflection.get(map, TLM_TABLE_FIELD) : TLME_EMPTY_ARRAY;
    }

    Stream<Reference<ThreadLocal<?>>> stream() {
        return Arrays.stream(table()).filter(Objects::nonNull).filter(NON_NULL_REFERENCE);
    }

    static final Predicate<Reference<?>> NON_NULL_REFERENCE = ref -> ref.get() != null;

    static final Method TL_GET_MAP_METHOD =
        Reflection.getMethod(ThreadLocal.class, "getMap", Thread.class);
    static final Method TL_CREATE_MAP_METHOD =
        Reflection.getMethod(ThreadLocal.class, "createMap", Thread.class, Object.class);

    static final Class<?> TLM_CLASS =
        Reflection.getClass("java.lang.ThreadLocal$ThreadLocalMap");
    static final Field TLM_TABLE_FIELD =
        Reflection.getField(TLM_CLASS, "table");
    static final Method TLM_GET_ENTRY_METHOD =
        Reflection.getMethod(TLM_CLASS, "getEntry", ThreadLocal.class);
    static final Method TLM_SET_METHOD =
        Reflection.getMethod(TLM_CLASS, "set", ThreadLocal.class, Object.class);
    static final Method TLM_REMOVE_METHOD =
        Reflection.getMethod(TLM_CLASS, "remove", ThreadLocal.class);

    static final Class<?> TLME_CLASS =
        Reflection.getClass("java.lang.ThreadLocal$ThreadLocalMap$Entry");
    static final WeakReference<ThreadLocal<?>>[] TLME_EMPTY_ARRAY =
        (WeakReference[]) Array.newInstance(TLME_CLASS, 0);
    static final Field TLME_VALUE_FIELD =
        Reflection.getField("java.lang.ThreadLocal$ThreadLocalMap$Entry", "value");

    static class Reflection {

        static Class<?> getClass(String className) {
            try {
                return Class.forName(className);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        static Field getField(String className, String fieldName) {
            return getField(getClass(className), fieldName);
        }

        static Field getField(Class<?> clazz, String fieldName) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field;
            }
            catch (NoSuchFieldException e) {
                return callSuper(clazz, c -> getField(c, fieldName), e);
            }
        }

        static <X> X get(Object target, Field field) {
            try {
                return (X) field.get(target);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        static Method getMethod(String className, String methodName, Class<?>... parameterTypes) {
            return getMethod(getClass(className), methodName, parameterTypes);
        }

        static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
            try {
                Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method;
            }
            catch (NoSuchMethodException e) {
                return callSuper(clazz, c -> getMethod(c, methodName, parameterTypes), e);
            }
        }

        static <X> X invoke(Object target, Method method, Object... args) {
            try {
                return (X) method.invoke(target, args);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        static <X> X callSuper(Class<?> clazz, Function<Class<?>, X> function, Throwable error) {
            Class<?> zuper = clazz.getSuperclass();
            if (zuper != null) {
                try {
                    return function.apply(zuper);
                }
                catch (Exception ignore) {
                }
            }
            throw new RuntimeException(error);
        }

    }

}

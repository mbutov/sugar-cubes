package org.sugarcubes.valueholder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Supplier;

/**
 * {@link ValueHolder}, ассоциированный с данным контекстом. Можно использовать, например, для значений в контексте
 * веб-сессии. Жизненный цикл хранимого значения не превышает жизненный цикл контекста. При удалении GC объекта контекста,
 * значение становится недоступным. В случае, если контекстом является поток, получаем аналог {@link ThreadLocal}.
 *
 * @author Maxim Butov
 */
public class ContextValueHolder<T> extends AbstractValueHolder<T> {

    private static final Map<Object, Map> VALUES = new WeakHashMap<Object, Map>();

    /**
     * Используется для того, чтобы удалить все значения, зарегистрированные для данного контекста.
     *
     * @param context контекст
     */
    protected static void removeContext(Object context) {
        VALUES.remove(context);
    }

    private final Supplier context;
    private final Object key;

    /**
     * Создаёт {@link ValueHolder}, ассоциированный с данным контекстом и ключом. Ключ нужен, чтобы в рамках одного контекста
     * можно было использовать несколько {@link ValueHolder}-ов. Если ключ не передаётся, то ключом будет сам
     * экземпляр {@link ValueHolder}-а.
     *
     * @param context контекст
     * @param key композитный ключ
     */
    public ContextValueHolder(Supplier context, Object... key) {
        Objects.requireNonNull(context, "context must not be null");

        this.context = context;

        if (key.length == 0) {
            this.key = this;
        }
        else {
            List<?> keyList = new ArrayList<>(Arrays.asList(key));
            if (keyList.contains(null)) {
                throw new IllegalArgumentException("Key cannot be null");
            }
            this.key = keyList;
        }
    }

    /**
     * Создаёт {@link ValueHolder}, ассоциированный с данным контекстом и ключом. Ключ нужен, чтобы в рамках одного контекста
     * можно было использовать несколько {@link ValueHolder}-ов. Если ключ не передаётся, то ключом будет сам
     * экземпляр {@link ValueHolder}-а.
     *
     * @param context контекст
     * @param key композитный ключ
     */
    public ContextValueHolder(Object context, Object... key) {
        this(new ReferenceSupplier(new WeakReference(context)), key);
        Objects.requireNonNull(context, "context must not be null");
    }

    private Object getContext() {
        Object context = this.context.get();
        Objects.requireNonNull(context, "context must not be null");
        return context;
    }

    private Map<Object, T> getMap() {
        final Object context = getContext();
        Map map = VALUES.get(context);
        if (map == null) {
            synchronized (context) {
                map = VALUES.get(context);
                if (map == null) {
                    map = new HashMap();
                    VALUES.put(context, map);
                }
            }
        }
        return map;
    }

    @Override
    public T get() {
        return getMap().get(key);
    }

    @Override
    public T set(T value) {
        return getMap().put(key, value);
    }

    @Override
    public T remove() {
        return getMap().remove(key);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ContextValueHolder)) {
            return false;
        }

        ContextValueHolder that = (ContextValueHolder) obj;

        return context.equals(that.context) && key.equals(that.key);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] {context, key});
    }

}

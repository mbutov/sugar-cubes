package org.sugarcubes.valueholder;

import java.util.function.Supplier;

/**
 * Фабрика {@link ValueHolder}-ов.
 *
 * @author Maxim Butov
 */
public class ValueHolders {

    private static final Object GLOBAL_CONTEXT = new Object();

    /**
     * @return {@link ValueHolder} для совместно используемого значения
     */
    public static <T> ValueHolder<T> global(Object... key) {
        return custom(() -> GLOBAL_CONTEXT, key);
    }

    /**
     * @return {@link ValueHolder} для значения в контексте потока
     */
    public static <T> ValueHolder<T> thread(Object... key) {
        return custom(Thread::currentThread, key);
    }

    /**
     * Возвращает {@link ValueHolder} для синглтона в контексте потока.
     *
     * @param type тип синглтона
     * @return {@link ValueHolder} для синглтона в контексте потока
     */
    public static <T> ValueHolder<T> threadSingleton(Class<T> type) {
        return thread(type).withInitial(new BeanInstantiator<T>(type));
    }

    /**
     * @param context провайдер контекста
     * @param key ключ в рамках контекста
     * @return {@link ValueHolder}, ассоциированный с данным контекстом и ключом
     * @see ContextValueHolder
     */
    public static <T> ValueHolder<T> custom(Supplier context, Object... key) {
        return new ContextValueHolder<T>(context, key);
    }

    /**
     * @param context контекст
     * @param key ключ в рамках контекста
     * @return {@link ValueHolder}, ассоциированный с данным контекстом и ключом
     * @see ContextValueHolder
     */
    public static <T> ValueHolder<T> custom(Object context, Object... key) {
        return custom(() -> context, key);
    }

}

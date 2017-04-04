package org.sugarcubes.valueholder;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * Интерфейс обёртки для некоторого значения, привязанного к контексту.
 * Обёртки могут иметь различный контекст (shared, thread, custom) и, следовательно, жизненный цикл.
 *
 * @author Maxim Butov
 */
public interface ValueHolder<T> extends Serializable {

    /**
     * Ключ для разделения значения в рамках контекста. Позволяет использовать следующие конструкции:
     *
     * <code>
     *     ValueHolders.context(request.getSession(), new ValueHolders.Key() {})
     * </code>
     *
     * Все {@link ValueHolder}-ы, созданные в данной строке кода будут иметь одинаковый ключ в рамках
     * контекста <code>request.getSession()</code>.
     *
     * Аналог {@link org.apache.wicket.MetaDataKey}.
     */
    abstract class Key<T> {

        @Override
        public final boolean equals(Object that) {
            return that != null && getClass().equals(that.getClass());
        }

        @Override
        public final int hashCode() {
            return getClass().hashCode();
        }

    }

    /**
     * Устанавливает данный {@link Supplier} в {@link ValueHolder} в качестве поставщика начальных значений.
     *
     * @param supplier поставщик
     *
     * @return этот же {@link ValueHolder}
     */
    <V> ValueHolder<V> withInitial(Supplier<V> supplier);

    /**
     * Инициализирует {@link ValueHolder}.
     *
     * @return этот же {@link ValueHolder}
     */
    <V> ValueHolder<V> initialize();

    /**
     * Инициализирует {@link ValueHolder} данным значением.
     *
     * @return этот же {@link ValueHolder}
     */
    <V> ValueHolder<V> initialize(V value);

    /**
     * @return текущее значение, возможно <code>null</code>
     */
    T get();

    /**
     * @return текущее значение, если <code>null</code>, то генерирует ошибку
     * @throws IllegalStateException если текущее значение null
     */
    T required();

    /**
     * {@link ValueHolder} будет заново проинициализирован, старое значение теряется.
     *
     * @return новое значение
     */
    T create();

    /**
     * @return текущее значение, если текущее значение <code>null</code> и установлен поставщик, то {@link ValueHolder} будет
     * проинициализирован
     */
    T getOrCreate();

    /**
     * Устанавливает значение.
     *
     * @param value новое значение
     * @return старое значение
     */
    T set(T value);

    /**
     * Очищает значение и освобождает ресурсы, использеумые для хранения значение.
     *
     * @return старое значение
     */
    T remove();

}

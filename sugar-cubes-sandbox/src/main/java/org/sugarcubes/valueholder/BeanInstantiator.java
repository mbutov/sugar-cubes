package org.sugarcubes.valueholder;

import java.util.function.Supplier;

/**
 * {@link Supplier}, создающий новые экземпляры класса.
 *
 * @author Maxim Butov
 */
public class BeanInstantiator<T> implements Supplier<T> {

    private final Class<T> type;

    /**
     * Конструктор.
     *
     * @param type класс для "размножения"
     */
    public BeanInstantiator(Class<T> type) {
        this.type = type;
    }

    @Override
    public T get() {
        // todo: use objenesis or smth
        try {
            return type.newInstance();
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}

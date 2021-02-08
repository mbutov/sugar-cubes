package org.sugarcubes.math;

import java.util.Comparator;

/**
 * @author Maxim Butov
 */
public abstract class NumberField<T> {

    public abstract T getZero();

    public abstract T getOne();

    public abstract T add(T left, T right);

    public T subtract(T left, T right) {
        return add(left, negate(right));
    }

    public T negate(T t) {
        return subtract(getZero(), t);
    }

    public abstract T multiply(T left, T right);

    public T reverse(T t) {
        return divide(getOne(), t);
    }

    public T divide(T left, T right) {
        return multiply(left, reverse(right));
    }

    public Comparator<T> getComparator() {
        return (Comparator) Comparator.naturalOrder();
    }

}

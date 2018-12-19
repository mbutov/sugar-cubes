package org.sugarcubes.lambda.serializable;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * {@link Predicate} + {@link Serializable}
 *
 * @author Maxim Butov
 */
public interface SPredicate<T> extends Predicate<T>, Serializable {

    static <X> SPredicate<X> nil() {
        return null;
    }

    static <X> SPredicate<X> always() {
        return x -> true;
    }

    static <X> SPredicate<X> never() {
        return x -> false;
    }

}

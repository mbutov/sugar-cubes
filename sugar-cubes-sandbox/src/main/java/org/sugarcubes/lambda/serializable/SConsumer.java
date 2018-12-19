package org.sugarcubes.lambda.serializable;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * {@link Consumer} + {@link Serializable}
 *
 * @author Maxim Butov
 */
public interface SConsumer<T> extends Consumer<T>, Serializable {

    SConsumer NOOP = (t) -> {
    };

    static <T> SConsumer<T> noop() {
        return NOOP;
    }

    default SConsumer<T> then(SConsumer<? super T> after) {
        return (T t) -> {
            accept(t);
            after.accept(t);
        };
    }

}

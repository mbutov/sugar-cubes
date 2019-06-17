package org.sugarcubes.function;

import java.util.function.Function;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public class Cast<X> implements Function<Object, X> {

    private static final Cast INSTANCE = new Cast();

    public static <X> Cast<X> instance() {
        return INSTANCE;
    }

    @Override
    public X apply(Object t) {
        return (X) t;
    }

}

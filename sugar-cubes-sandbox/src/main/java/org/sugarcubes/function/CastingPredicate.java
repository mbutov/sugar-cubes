package org.sugarcubes.function;

import java.util.function.Predicate;

/**
 * todo: document it
 *
 * @author Maxim Butov
 */
public interface CastingPredicate<X, Y extends X> extends Predicate<X> {

    class Impl<X, Y extends X> implements CastingPredicate<X, Y> {

        private final Class<Y> type;

        Impl(Class<Y> type) {
            this.type = type;
        }

        @Override
        public boolean test(X obj) {
            return type.isInstance(obj);
        }

    }

    static <X, Y extends X> CastingPredicate<X, Y> is(Class<Y> type) {
        return new Impl<>(type);
    }

    default Y cast(X x) {
        return (Y) x;
    }

}

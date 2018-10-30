package org.sugarcubes.reflection;

import java.util.Optional;
import java.util.stream.Collector;

/**
 * Useful collectors.
 *
 * @author Maxim Butov
 */
public class XCollectors {

    static class CollectorState<X> {

        boolean isSet;
        X value;

        void checkIsSet(boolean expected) {
            if (isSet != expected) {
                throw new IllegalStateException(isSet ? "Many elements" : "No elements");
            }
        }

        void accumulate(X next) {
            checkIsSet(false);
            isSet = true;
            value = next;
        }

        CollectorState<X> combine(CollectorState<X> other) {
            if (other.isSet) {
                checkIsSet(false);
                return other;
            }
            return this;
        }

        Optional<X> toOptional() {
            return Optional.ofNullable(value);
        }

        X toOnlyElement() {
            checkIsSet(true);
            return value;
        }

    }

    public static <X> Collector<X, CollectorState<X>, Optional<X>> toOptional() {
        return Collector.of(CollectorState::new, CollectorState::accumulate, CollectorState::combine, CollectorState::toOptional,
            Collector.Characteristics.UNORDERED);
    }

    public static <X> Collector<X, CollectorState<X>, X> toOnlyElement() {
        return Collector.of(CollectorState::new, CollectorState::accumulate, CollectorState::combine, CollectorState::toOnlyElement,
            Collector.Characteristics.UNORDERED);
    }

}

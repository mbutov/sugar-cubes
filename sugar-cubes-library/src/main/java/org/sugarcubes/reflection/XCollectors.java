package org.sugarcubes.reflection;

import java.util.Deque;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Useful collectors.
 *
 * @author Maxim Butov
 */
class XCollectors {

    private static final Supplier<RuntimeException> ILLEGAL_COLLECTOR_STATE =
        () -> new IllegalStateException("Illegal collector state.");

    private static final Supplier<RuntimeException> TOO_MANY_ELEMENTS_EXCEPTION =
        () -> new IllegalStateException("Stream contains two or more elements.");

    private static final Supplier<RuntimeException> NO_ELEMENTS_EXCEPTION =
        () -> new NoSuchElementException("No elements found.");

    private static class CollectorState<X> {

        final Deque<X> values = new LinkedList<>();

        final Supplier<? extends RuntimeException> tooManyElementsException;
        final Supplier<? extends RuntimeException> noElementsException;

        public CollectorState(Supplier<? extends RuntimeException> tooManyElementsException, Supplier<? extends RuntimeException> noElementsException) {
            this.tooManyElementsException = tooManyElementsException;
            this.noElementsException = noElementsException;
        }

        void accumulate(X next) {
            if (!values.isEmpty()) {
                throw tooManyElementsException.get();
            }
            values.add(next);
        }

        CollectorState<X> combine(CollectorState<X> other) {
            other.values.forEach(this::accumulate);
            return this;
        }

        Optional<X> toOptional() {
            return values.stream().findAny();
        }

        X onlyElement() {
            if (values.isEmpty()) {
                throw noElementsException.get();
            }
            return values.element();
        }

    }

    /**
     * Collector which can be applied to stream of zero or one element. Returns empty or filled in {@link Optional}.
     *
     * @return optional
     *
     * @throws IllegalStateException if stream contains more than one element
     */
    public static <X> Collector<X, CollectorState<X>, Optional<X>> toOptional(Supplier<? extends RuntimeException> tooManyElementsException) {
        return Collector.of(
            () -> new CollectorState<>(tooManyElementsException, ILLEGAL_COLLECTOR_STATE),
            CollectorState::accumulate,
            CollectorState::combine,
            CollectorState::toOptional,
            Collector.Characteristics.UNORDERED);
    }

    /**
     * Collector which can be applied to stream of zero or one element. Returns empty or filled in {@link Optional}.
     *
     * @return optional
     *
     * @throws IllegalStateException if stream contains more than one element
     */
    public static <X> Collector<X, CollectorState<X>, Optional<X>> toOptional() {
        return toOptional(TOO_MANY_ELEMENTS_EXCEPTION);
    }

    /**
     * Collector which can be applied to stream of single element.
     *
     * @return the single element of stream
     *
     * @throws NoSuchElementException if stream contains no elements
     * @throws IllegalStateException if stream contains more than one element
     */
    public static <X> Collector<X, CollectorState<X>, X> onlyElement(Supplier<? extends RuntimeException> tooManyElementsException, Supplier<? extends RuntimeException> noElementsException) {
        return Collector.of(
            () -> new CollectorState<>(tooManyElementsException, noElementsException),
            CollectorState::accumulate,
            CollectorState::combine,
            CollectorState::onlyElement,
            Collector.Characteristics.UNORDERED);
    }

    /**
     * Collector which can be applied to stream of single element.
     *
     * @return the single element of stream
     *
     * @throws NoSuchElementException if stream contains no elements
     * @throws IllegalStateException if stream contains more than one element
     */
    public static <X> Collector<X, CollectorState<X>, X> onlyElement() {
        return onlyElement(TOO_MANY_ELEMENTS_EXCEPTION, NO_ELEMENTS_EXCEPTION);
    }

}

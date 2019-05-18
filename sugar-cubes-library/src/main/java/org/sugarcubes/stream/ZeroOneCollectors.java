package org.sugarcubes.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Collectors for streams with zero or one elements.
 *
 * @author Maxim Butov
 */
public class ZeroOneCollectors {

    private static final Supplier<RuntimeException> ILLEGAL_COLLECTOR_STATE = () -> {
        throw new AssertionError("Must not happen.");
    };

    private static final Supplier<RuntimeException> TOO_MANY_ELEMENTS_EXCEPTION =
        () -> new IllegalStateException("Stream contains more than one elements.");

    private static final Supplier<RuntimeException> NO_ELEMENTS_EXCEPTION =
        () -> new NoSuchElementException("No elements found.");

    private static class CollectorState<X> {

        final List<X> values = new ArrayList<>(1);

        final Supplier<? extends RuntimeException> tooManyElementsException;
        final Supplier<? extends RuntimeException> noElementsException;

        CollectorState(Supplier<? extends RuntimeException> tooManyElementsException, Supplier<? extends RuntimeException> noElementsException) {
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
            return values.get(0);
        }

    }

    private static <X, Y> Collector<X, ?, Y> collector(
        Supplier<? extends RuntimeException> tooManyElementsException, Supplier<? extends RuntimeException> noElementsException,
        Function<CollectorState<X>, Y> finisher) {

        return Collector.of(
            () -> new CollectorState<>(tooManyElementsException, noElementsException),
            CollectorState::accumulate,
            CollectorState::combine,
            finisher,
            Collector.Characteristics.UNORDERED);
    }

    /**
     * Collector which can be applied to stream of zero or one element. Returns empty or filled in {@link Optional}.
     *
     * @return optional
     *
     * @throws IllegalStateException if stream contains more than one element
     */
    public static <X> Collector<X, ?, Optional<X>> toOptional(Supplier<? extends RuntimeException> tooManyElementsException) {
        return collector(tooManyElementsException, ILLEGAL_COLLECTOR_STATE, CollectorState::toOptional);
    }

    /**
     * Collector which can be applied to stream of zero or one element. Returns empty or filled in {@link Optional}.
     *
     * @return optional
     *
     * @throws IllegalStateException if stream contains more than one element
     */
    public static <X> Collector<X, ?, Optional<X>> toOptional() {
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
    public static <X> Collector<X, ?, X> onlyElement(Supplier<? extends RuntimeException> tooManyElementsException, Supplier<? extends RuntimeException> noElementsException) {
        return collector(tooManyElementsException, noElementsException, CollectorState::onlyElement);
    }

    /**
     * Collector which can be applied to stream of single element.
     *
     * @return the single element of stream
     *
     * @throws NoSuchElementException if stream contains no elements
     * @throws IllegalStateException if stream contains more than one element
     */
    public static <X> Collector<X, ?, X> onlyElement() {
        return onlyElement(TOO_MANY_ELEMENTS_EXCEPTION, NO_ELEMENTS_EXCEPTION);
    }

}

package org.sugarcubes.reflection;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collector;

/**
 * Useful collectors.
 *
 * @author Maxim Butov
 */
public class XCollectors {

    static class CollectorState<X> {

        Queue<X> values = new LinkedList<>();

        void accumulate(X next) {
            if (!values.isEmpty()) {
                throw new IllegalStateException("Stream contains two or more elements");
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
                throw new NoSuchElementException("No elements found.");
            }
            return values.element();
        }

    }

    private static final Collector<Object, CollectorState<Object>, Optional> TO_OPTIONAL_COLLECTOR = Collector.of(
        CollectorState::new,
        CollectorState::accumulate,
        CollectorState::combine,
        CollectorState::toOptional,
        Collector.Characteristics.UNORDERED);

    private static final Collector<Object, CollectorState<Object>, Object> ONLY_ELEMENT_COLLECTOR = Collector.of(
        CollectorState::new,
        CollectorState::accumulate,
        CollectorState::combine,
        CollectorState::onlyElement,
        Collector.Characteristics.UNORDERED);

    /**
     * Collector which can be applied to stream of zero or one element. Returns empty or filled in {@link Optional}.
     *
     * @return optional
     *
     * @throws IllegalStateException if stream contains more than one element
     */
    public static <X> Collector<X, CollectorState<X>, Optional<X>> toOptional() {
        return (Collector) TO_OPTIONAL_COLLECTOR;
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
        return (Collector) ONLY_ELEMENT_COLLECTOR;
    }

}

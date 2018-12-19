package org.sugarcubes.tuple;

import java.util.List;

/**
 * Tuple.
 *
 * See <a href="https://en.wikipedia.org/wiki/Tuple">Tuple in wikipedia</a>.
 *
 * Tuple is a kind of {@link java.util.List} which is:
 *
 * <ul>
 *     <li>immutable</li>
 *     <li>all its elements are not null</li>
 *     <li>comparable in a lexicographical order</li>
 * </ul>
 *
 * Tuples can be used as complex keys in maps, caches, etc.
 *
 * @author Maxim Butov
 */
public interface Tuple<T> extends List<T> {

    @Override
    Tuple<T> subList(int fromIndex, int toIndex);

}

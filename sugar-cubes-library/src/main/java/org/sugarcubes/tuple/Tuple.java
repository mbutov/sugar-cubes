package org.sugarcubes.tuple;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;

/**
 * Tuple implementation.
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
 * Tuple is not cloneable because it is immutable.
 *
 * @author Maxim Butov
 */
public class Tuple<T> extends AbstractList<T> implements RandomAccess, Serializable, Comparable<Tuple<T>> {

    private static final long serialVersionUID = 1L;

    private final T[] values;

    public Tuple(T... values) {
        this(false, values.clone());
    }

    public Tuple(Collection<T> values) {
        this(false, values.toArray());
    }

    protected Tuple(boolean unused, Object[] values) {
        for (Object value : values) {
            Objects.requireNonNull(value, "Values contains null");
        }
        this.values = (T[]) values;
    }

    @Override
    public T get(int index) {
        return values[index];
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public Tuple<T> subList(int fromIndex, int toIndex) {
        return new Tuple<T>(false, Arrays.copyOfRange(values, fromIndex, toIndex));
    }

    @Override
    public Object[] toArray() {
        return values.clone();
    }

    @Override
    public <V> V[] toArray(V[] array) {
        if (array.length == values.length) {
            System.arraycopy(values, 0, array, 0, values.length);
            return array;
        }
        else {
            return Arrays.copyOf(values, values.length, (Class<V[]>) array.getClass());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Tuple)) {
            return false;
        }
        Tuple that = (Tuple) obj;
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public int compareTo(Tuple<T> that) {

        int s1 = size();
        int s2 = that.size();

        int n = Math.min(s1, s2);

        for (int k = 0; k < n; k++) {
            int r = ((Comparable) get(k)).compareTo(that.get(k));
            if (r != 0) {
                return r;
            }
        }

        return Integer.compare(s1, s2);

    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + Arrays.toString(values);
    }

}

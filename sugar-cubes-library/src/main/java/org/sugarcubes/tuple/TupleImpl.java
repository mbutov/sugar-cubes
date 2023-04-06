package org.sugarcubes.tuple;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.RandomAccess;

import org.sugarcubes.check.Checks;

/**
 * {@link Tuple} implementation.
 *
 * @author Maxim Butov
 */
public class TupleImpl<T> extends AbstractList<T> implements Tuple<T>, RandomAccess, Comparable<Tuple<T>>, Serializable {

    private static final long serialVersionUID = 1L;

    private final Object[] values;

    public TupleImpl(T... values) {
        this(true, values);
    }

    public TupleImpl(Collection<T> values) {
        this(false, values.toArray());
    }

    protected TupleImpl(boolean clone, Object[] values) {
        if (Arrays.stream(values).anyMatch(Objects::isNull)) {
            throw Checks.arg().fail("Values contains null");
        }
        this.values = clone ? values.clone() : values;
    }

    @Override
    public T get(int index) {
        return (T) values[index];
    }

    @Override
    public int size() {
        return values.length;
    }

    @Override
    public Tuple<T> subList(int fromIndex, int toIndex) {
        return new TupleImpl<>(false, Arrays.copyOfRange(values, fromIndex, toIndex));
    }

    @Override
    public Object[] toArray() {
        return values.clone();
    }

    @Override
    public <V> V[] toArray(V[] array) {
        return Arrays.asList(values).toArray(array);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TupleImpl)) {
            return false;
        }
        TupleImpl that = (TupleImpl) obj;
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public int compareTo(Tuple<T> that) {
        return TupleComparator.<T>naturalOrder().compare(this, that);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + Arrays.toString(values);
    }

}

package org.sugarcubes;

import java.util.Comparator;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * todo: document it and adjust author
 *
 * @author Q-MBU
 */
public class EqualsHelper {

    public static final BiPredicate<Class, Class> SAME_CLASS = Class::equals;
    public static final BiPredicate<Class, Class> IS_ASSIGNABLE_FROM = Class::isAssignableFrom;

    public static <T> boolean genericEquals(
        T self, Object that, BiPredicate<T, T> instancePredicate,
        BiPredicate<Class, Class> classPredicate, Class<T> selfClass
    ) {
        if (self == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        return classPredicate.test(selfClass, that.getClass()) && instancePredicate.test(self, (T) that);
    }

    public static <T> boolean genericEquals(
        T self, Object that, BiPredicate<T, T> instancePredicate,
        BiPredicate<Class, Class> classPredicate
    ) {
        return genericEquals(self, that, instancePredicate, classPredicate, (Class) self.getClass());
    }

    public static <T> boolean equalsSameClass(T self, Object that, BiPredicate<T, T> predicate) {
        return genericEquals(self, that, predicate, SAME_CLASS, (Class) self.getClass());
    }

    public static <T> boolean equalsIsInstance(T self, Object that, BiPredicate<T, T> predicate) {
        return genericEquals(self, that, predicate, IS_ASSIGNABLE_FROM, (Class) self.getClass());
    }

    public static <T> boolean equalsSameClass(T self, Object that, Predicate<T> predicate) {
        return equalsSameClass(self, that, unaryToBiPredicate(predicate));
    }

    public static <T> boolean equalsIsInstance(T self, Object that, Predicate<T> predicate) {
        return equalsIsInstance(self, that, unaryToBiPredicate(predicate));
    }

    public static <T> boolean equalsSameClass(T self, Object that, Comparator<T> comparator) {
        return equalsSameClass(self, that, comparatorToBiPredicate(comparator));
    }

    public static <T> boolean equalsIsInstance(T self, Object that, Comparator<T> comparator) {
        return equalsIsInstance(self, that, comparatorToBiPredicate(comparator));
    }

    private static <T> BiPredicate<T, T> unaryToBiPredicate(Predicate<T> predicate) {
        return (first, second) -> predicate.test(second);
    }

    private static <T> BiPredicate<T, T> comparatorToBiPredicate(Comparator<T> comparator) {
        return (first, second) -> comparator.compare(first, second) == 0;
    }

}

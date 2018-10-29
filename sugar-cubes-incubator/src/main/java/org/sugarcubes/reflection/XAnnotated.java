package org.sugarcubes.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Common part of {@link AnnotatedElement}'s wrappers.
 *
 * @author Maxim Butov
 */
public interface XAnnotated<T extends AnnotatedElement> extends XReflectionObject<T> {

    Stream<? extends Annotation> getDeclaredAnnotations();

    default Stream<? extends Annotation> getAnnotations() {
        return getDeclaredAnnotations();
    }

    default <X extends Annotation> Optional<X> getAnnotation(Class<X> type) {
        // todo
        return null;
    }

}

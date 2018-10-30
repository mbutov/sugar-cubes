package org.sugarcubes.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Common part of {@link AnnotatedElement}'s wrappers.
 *
 * @author Maxim Butov
 */
public interface XAnnotated<T extends AnnotatedElement> extends XReflectionObject<T> {

    default Stream<? extends Annotation> getDeclaredAnnotations() {
        return Arrays.stream(getReflectionObject().getDeclaredAnnotations());
    }

    default <X extends Annotation> Optional<X> getDeclaredAnnotation(Class<X> annotationClass) {
        return Optional.ofNullable(getReflectionObject().getDeclaredAnnotation(annotationClass));
    }

    default Stream<? extends Annotation> getAnnotations() {
        return Arrays.stream(getReflectionObject().getAnnotations());
    }

    default <X extends Annotation> Optional<X> getAnnotation(Class<X> annotationClass) {
        return Optional.ofNullable(getReflectionObject().getAnnotation(annotationClass));
    }

}

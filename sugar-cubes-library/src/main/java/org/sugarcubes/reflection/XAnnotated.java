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
public interface XAnnotated<AE extends AnnotatedElement> extends XReflectionObject<AE> {

    default Stream<? extends Annotation> getDeclaredAnnotations() {
        return Arrays.stream(getReflectionObject().getDeclaredAnnotations());
    }

    default <X extends Annotation> Optional<X> getDeclaredAnnotation(Class<X> annotationClass) {
        return Optional.ofNullable(getReflectionObject().getDeclaredAnnotation(annotationClass));
    }

    default <X extends Annotation> Stream<X> getDeclaredAnnotationsByType(Class<X> annotationClass) {
        return Arrays.stream(getReflectionObject().getDeclaredAnnotationsByType(annotationClass));
    }

    default Stream<? extends Annotation> getAnnotations() {
        return Arrays.stream(getReflectionObject().getAnnotations());
    }

    default <X extends Annotation> Optional<X> getAnnotation(Class<X> annotationClass) {
        return Optional.ofNullable(getReflectionObject().getAnnotation(annotationClass));
    }

    default <X extends Annotation> Stream<X> getAnnotationsByType(Class<X> annotationClass) {
        return Arrays.stream(getReflectionObject().getAnnotationsByType(annotationClass));
    }

}

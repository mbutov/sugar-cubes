package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.util.Arrays.stream;

import org.sugarcubes.builder.collection.ListBuilder;
import org.sugarcubes.builder.collection.SetBuilder;

/**
 * @author Maxim Butov
 */
class XClassUtils {

    static XClass<?> getSuperclass(XClass<?> xClass) {
        Class<?> superclass = xClass.getReflectionObject().getSuperclass();
        return superclass != null ? XReflection.of(superclass) : XNullClass.INSTANCE;
    }

    static List<XClass<?>> getInheritance(XClass<?> xClass) {
        return ListBuilder.<XClass<?>>arrayList()
            .add(xClass)
            .addAll(xClass.getSuperclass().getInheritance())
            .transform(Collections::unmodifiableList)
            .build();
    }

    static List<XClass<?>> getDeclaredInterfaces(XClass<?> xClass) {
        return ListBuilder.<XClass<?>>arrayList()
            .addAll(stream(xClass.getReflectionObject().getInterfaces()).map(XReflection::of))
            .transform(Collections::unmodifiableList)
            .build();
    }

    static List<XClass<?>> getInterfaces(XClass<?> xClass) {
        return SetBuilder.<XClass<?>>linkedHashSet()
            .addAll(xClass.getDeclaredInterfaces())
            .addAll(xClass.getSuperclass().getInterfaces())
            .transform(ArrayList::new)
            .transform(Collections::unmodifiableList)
            .build();
    }

    static <C> List<XConstructor<C>> getConstructors(XClass<C> xClass) {
        Constructor<C>[] constructors = (Constructor[]) xClass.getReflectionObject().getDeclaredConstructors();
        return ListBuilder.<XConstructor<C>>arrayList()
            .addAll(stream(constructors).map(XReflection::of))
            .transform(Collections::unmodifiableList)
            .build();
    }

    static List<XField<?>> getDeclaredFields(XClass<?> xClass) {
        return ListBuilder.<XField<?>>arrayList()
            .addAll(stream(xClass.getReflectionObject().getDeclaredFields()).map(XReflection::of))
            .transform(Collections::unmodifiableList)
            .build();
    }

    static List<XField<?>> getFields(XClass<?> xClass) {
        return ListBuilder.<XField<?>>arrayList()
            .addAll(xClass.getDeclaredFields())
            .addAll(xClass.getSuperclass().getFields())
            .transform(Collections::unmodifiableList)
            .build();
    }

}

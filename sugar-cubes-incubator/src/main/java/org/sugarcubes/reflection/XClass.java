package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Wrapper for {@link Class}.
 *
 * @author Maxim Butov
 */
public class XClass<C> extends XReflectionObjectImpl<Class<C>> implements XAnnotated<Class<C>>, XModifiers {

    private final Class<C> reflectionObject;

    XClass(Class<C> reflectionObject) {
        this.reflectionObject = reflectionObject;
    }

    @Override
    public Class<C> getReflectionObject() {
        return reflectionObject;
    }

    @Override
    public int getModifiers() {
        return getReflectionObject().getModifiers();
    }

    @Override
    public String getName() {
        return getReflectionObject().getName();
    }

    public XClass<?> getSuperclass() {
        Class<?> superclass = getReflectionObject().getSuperclass();
        return superclass != null ? XReflection.of(superclass) : XNullClass.INSTANCE;
    }

    public boolean isNull() {
        return false;
    }

    public Stream<XClass<?>> getInheritance() {
        return Stream.concat(Stream.of(this), getSuperclass().getInheritance());
    }

    public Stream<XClass<?>> getDeclaredInterfaces() {
        return Arrays.stream(getReflectionObject().getInterfaces()).map(XReflection::of);
    }

    public Stream<XClass<?>> getInterfaces() {
        return Stream.concat(getDeclaredInterfaces(), getSuperclass().getInterfaces()).distinct();
    }

    public Stream<XConstructor<C>> getDeclaredConstructors() {
        return Arrays.stream((Constructor<C>[]) getReflectionObject().getDeclaredConstructors())
            .map(XReflection::of);
    }

    public Stream<XConstructor<C>> getConstructors() {
        return getDeclaredConstructors();
    }

    public Stream<XField<?>> getDeclaredFields() {
        return Arrays.stream(getReflectionObject().getDeclaredFields()).map(XReflection::of);
    }

    public Stream<XField<?>> getFields() {
        return Stream.concat(getDeclaredFields(), getSuperclass().getFields());
    }

    public Stream<XMethod<?>> getDeclaredMethods() {
        return Arrays.stream(getReflectionObject().getDeclaredMethods()).map(XReflection::of);
    }

    public Stream<XMethod<?>> getMethods() {
        return Stream.concat(getDeclaredMethods(), getSuperclass().getMethods());
    }

    public Optional<XConstructor<C>> findConstructor(Class... types) {
        return getDeclaredConstructors().filter(XPredicates.withParameterTypes(types)).findAny();
    }

    public XConstructor<C> getConstructor(Class... types) {
        return findConstructor(types).orElseThrow(XReflectiveOperationException.withMessage("Constructor not found"));
    }

    public <X> Optional<XField<X>> findField(String name) {
        return (Optional) getFields().filter(XPredicates.withName(name)).findFirst();
    }

    public <X> XField<X> getField(String name) {
        return this.<X>findField(name).orElseThrow(XReflectiveOperationException.withMessage("Field not found"));
    }

    public <X> Optional<XMethod<X>> findMethod(String name, Class... types) {
        return (Optional) getMethods()
            .filter(method -> method.hasNameAndParameterTypes(name, types))
            .findFirst();
    }

    public <X> XMethod<X> getMethod(String name, Class... types) {
        return this.<X>findMethod(name, types).orElseThrow(XReflectiveOperationException.withMessage("Method not found"));
    }

}

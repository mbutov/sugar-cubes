package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.Arrays.stream;

import static org.sugarcubes.reflection.XPredicates.withName;
import static org.sugarcubes.reflection.XPredicates.withParameterTypes;
import static org.sugarcubes.reflection.XReflectiveOperationException.withMessage;

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
        return stream(getReflectionObject().getInterfaces()).map(XReflection::of);
    }

    public Stream<XClass<?>> getInterfaces() {
        return Stream.concat(getDeclaredInterfaces(), getSuperclass().getInterfaces()).distinct();
    }

    public Stream<XConstructor<C>> getConstructors() {
        return stream((Constructor<C>[]) getReflectionObject().getDeclaredConstructors()).map(XReflection::of);
    }

    public Optional<XConstructor<C>> findConstructor(Class... types) {
        return find(getConstructors(), withParameterTypes(types));
    }

    public XConstructor<C> getConstructor(Class... types) {
        return findConstructor(types)
            .orElseThrow(withMessage(() -> String.format("Constructor %s(%s) not found", getName(), getParameterNames(types))));
    }

    public Stream<XField<?>> getDeclaredFields() {
        return stream(getReflectionObject().getDeclaredFields()).map(XReflection::of);
    }

    public <X> Optional<XField<X>> findDeclaredField(String name) {
        return find(getDeclaredFields(), withName(name));
    }

    public <X> XField<X> getDeclaredField(String name) {
        return this.<X>findDeclaredField(name).orElseThrow(withMessage("Field %s.%s not found", getName(), name));
    }

    public Stream<XField<?>> getFields() {
        return Stream.concat(getDeclaredFields(), getSuperclass().getFields());
    }

    public <X> Stream<XField<X>> findFields(String name) {
        return (Stream) getFields().filter(withName(name));
    }

    public <X> XField<X> getField(String name) {
        return this.<X>findFields(name).collect(XCollectors.onlyElement());
    }

    public Stream<XMethod<?>> getDeclaredMethods() {
        return stream(getReflectionObject().getDeclaredMethods()).map(XReflection::of);
    }

    public <X> Optional<XMethod<X>> findDeclaredMethod(String name, Class... types) {
        return find(getDeclaredMethods(), method -> method.hasNameAndParameterTypes(name, types));
    }

    public <X> XMethod<X> getDeclaredMethod(String name, Class... types) {
        return this.<X>findDeclaredMethod(name, types)
            .orElseThrow(withMessage(() -> String.format("Method %s.%s(%s) not found", getName(), name, getParameterNames(types))));
    }

    public Stream<XMethod<?>> getMethods() {
        return Stream.concat(getDeclaredMethods(), getSuperclass().getMethods());
    }

    public <X> Optional<XMethod<X>> findMethod(String name, Class... types) {
        return (Optional) getMethods().filter(method -> method.hasNameAndParameterTypes(name, types)).findFirst();
    }

    public <X> XMethod<X> getMethod(String name, Class... types) {
        return this.<X>findMethod(name, types)
            .orElseThrow(withMessage(() -> String.format("Method %s.%s(%s) not found in class hierarchy", getName(), name, getParameterNames(types))));
    }

    private static <T, V extends T> Optional<V> find(Stream<T> stream, Predicate<? super T> predicate) {
        return (Optional) stream.filter(predicate).collect(XCollectors.toOptional());
    }

    private static String getParameterNames(Class[] types) {
        return stream(types).map(Class::getName).collect(Collectors.joining(","));
    }

}

package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.Arrays.stream;

import static org.sugarcubes.reflection.XCollectors.onlyElement;
import static org.sugarcubes.reflection.XCollectors.toOptional;
import static org.sugarcubes.reflection.XPredicates.withName;
import static org.sugarcubes.reflection.XPredicates.withNameAndParameterTypes;
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
        Constructor<C>[] constructors = (Constructor<C>[]) getReflectionObject().getDeclaredConstructors();
        return stream(constructors).map(XReflection::of);
    }

    public Optional<XConstructor<C>> findConstructor(Class... types) {
        return getConstructors().filter(withParameterTypes(types)).collect(toOptional());
    }

    public XConstructor<C> getConstructor(Class... types) {
        return findConstructor(types)
            .orElseThrow(withMessage(() -> String.format("Constructor %s(%s) not found", getName(), getParameterNames(types))));
    }

    public Stream<XField<?>> getDeclaredFields() {
        return stream(getReflectionObject().getDeclaredFields()).map(XReflection::of);
    }

    public <X> Optional<XField<X>> findDeclaredField(String name) {
        Stream<XField<X>> fields = (Stream) getDeclaredFields();
        return fields.filter(withName(name)).collect(toOptional());
    }

    public <X> XField<X> getDeclaredField(String name) {
        Optional<XField<X>> field = findDeclaredField(name);
        return field.orElseThrow(withMessage("Field %s.%s not found", getName(), name));
    }

    public Stream<XField<?>> getFields() {
        return Stream.concat(getDeclaredFields(), getSuperclass().getFields());
    }

    public <X> Stream<XField<X>> findFields(String name) {
        Stream<XField<X>> fields = (Stream) getFields();
        return fields.filter(withName(name));
    }

    public <X> XField<X> getField(String name) {
        return this.<X>findFields(name).collect(onlyElement(
            withMessage("Two or more fields with name %s found in %s hierarchy", name, getName()),
            withMessage("No field %s found in %s hierarchy", name, getName())
        ));
    }

    public Stream<XMethod<?>> getDeclaredMethods() {
        return stream(getReflectionObject().getDeclaredMethods()).map(XReflection::of);
    }

    public <X> Optional<XMethod<X>> findDeclaredMethod(String name, Class... types) {
        Stream<XMethod<X>> methods = (Stream) getDeclaredMethods();
        return methods.filter(withNameAndParameterTypes(name, types)).collect(toOptional());
    }

    public <X> XMethod<X> getDeclaredMethod(String name, Class... types) {
        Optional<XMethod<X>> method = findDeclaredMethod(name, types);
        return method.orElseThrow(withMessage(() ->
            String.format("Method %s.%s(%s) not found", getName(), name, getParameterNames(types))));
    }

    public Stream<XMethod<?>> getMethods() {
        return Stream.concat(getDeclaredMethods(), getSuperclass().getMethods());
    }

    public <X> Optional<XMethod<X>> findMethod(String name, Class... types) {
        Stream<XMethod<X>> methods = (Stream) getMethods();
        return methods.filter(withNameAndParameterTypes(name, types)).findFirst();
    }

    public <X> XMethod<X> getMethod(String name, Class... types) {
        return this.<X>findMethod(name, types)
            .orElseThrow(withMessage(() -> String.format("Method %s.%s(%s) not found in class hierarchy", getName(), name, getParameterNames(types))));
    }

    private static String getParameterNames(Class[] types) {
        return stream(types).map(Class::getName).collect(Collectors.joining(","));
    }

}

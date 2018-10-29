package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XClass<T> extends XReflectionObject<Class<T>> implements XModifiers {

    XClass(Class<T> reflectionObject) {
        super(reflectionObject);
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
        return Optional.ofNullable(getReflectionObject().getSuperclass()).map(XClass::new).orElse(XNullClass.INSTANCE);
    }

    public Stream<XClass<?>> getDeclaredInterfaces() {
        return Arrays.stream(getReflectionObject().getInterfaces()).map(XClass::new);
    }

    public Stream<XClass<?>> getInterfaces() {
        return Stream.concat(getDeclaredInterfaces(), getSuperclass().getInterfaces()).distinct();
    }

    public XClassPackage getPackage() {
        return new XClassPackage(this);
    }

    public Stream<XConstructor<T>> getDeclaredConstructors() {
        return Arrays.stream((Constructor<T>[]) getReflectionObject().getDeclaredConstructors())
            .map(XConstructor::new);
    }

    public Stream<XConstructor<T>> getConstructors() {
        return getDeclaredConstructors();
    }

    public Stream<XField<?>> getDeclaredFields() {
        return Arrays.stream(getReflectionObject().getDeclaredFields()).map(XField::new);
    }

    public Stream<XField<?>> getFields() {
        return Stream.concat(getDeclaredFields(), getSuperclass().getFields());
    }

    public Stream<XMethod<?>> getDeclaredMethods() {
        return Arrays.stream(getReflectionObject().getDeclaredMethods()).map(XMethod::new);
    }

    public Stream<XMethod<?>> getMethods() {
        return Stream.concat(getDeclaredMethods(), getSuperclass().getMethods());
    }

    public Optional<XConstructor<T>> findConstructor(Class... types) {
        return getDeclaredConstructors().filter(XPredicates.withParameterTypes(types)).findAny();
    }

    public XConstructor<T> getConstructor(Class... types) {
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
            .filter(XPredicates.withName(name))
            .filter(XPredicates.withParameterTypes(types))
            .findFirst();
    }

    public <X> XMethod<X> getMethod(String name, Class... types) {
        return this.<X>findMethod(name, types).orElseThrow(XReflectiveOperationException.withMessage("Method not found"));
    }

}

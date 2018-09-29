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

    protected XClass(Class<T> javaObject) {
        super(javaObject);
    }

    @Override
    public int getModifiers() {
        return getReflectionObject().getModifiers();
    }

    @Override
    public String getName() {
        return getReflectionObject().getName();
    }

    public XClass<?> getSuperClass() {
        return Optional.ofNullable(getReflectionObject().getSuperclass()).map(XClass::new).orElse(XNullClass.INSTANCE);
    }

    public Stream<XClass<?>> getInterfaces() {
        return Arrays.stream(Optional.ofNullable(getReflectionObject().getInterfaces()).orElse(new Class[0])).map(XClass::new);
    }

    public Stream<XClass<?>> getAllInterfaces() {
        return Stream.concat(getInterfaces(), getSuperClass().getAllInterfaces()).distinct();
    }

    public XClassPackage getPackage() {
        return new XClassPackage(this);
    }

    public Stream<XConstructor<T>> getConstructors() {
        return Arrays.stream((Constructor<T>[]) getReflectionObject().getDeclaredConstructors())
            .map(XConstructor::new);
    }

    public Stream<XField<?>> getFields() {
        return Arrays.stream(getReflectionObject().getDeclaredFields()).map(XField::new);
    }

    public Stream<XField<?>> getAllFields() {
        return Stream.concat(getFields(), getSuperClass().getAllFields());
    }

    public Stream<XMethod<?>> getMethods() {
        return Arrays.stream(getReflectionObject().getDeclaredMethods()).map(XMethod::new);
    }

    public Stream<XMethod<?>> getAllMethods() {
        return Stream.concat(getMethods(), getSuperClass().getAllMethods());
    }

    public Optional<XConstructor<T>> findConstructor(Class... types) {
        return getConstructors().filter(constructor -> constructor.matches(types)).findAny();
    }

    public <X> Optional<XMethod<X>> findMethod(String name, Class... types) {
        return getAllMethods()
            .filter(method -> method.getName().equals(name))
            .filter(method -> method.matches(types))
            .map(method -> (XMethod<X>) method)
            .findAny();
    }

}

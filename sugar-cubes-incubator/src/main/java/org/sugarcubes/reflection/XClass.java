package org.sugarcubes.reflection;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * todo: document it and adjust author
 *
 * @author Maxim Butov
 */
public class XClass<T> implements XModifiers {

    private final Class<T> javaClass;

    public XClass(Class<T> javaClass) {
        this.javaClass = Objects.requireNonNull(javaClass);
    }

    public Class<T> getJavaClass() {
        return javaClass;
    }

    @Override
    public int getModifiers() {
        return javaClass.getModifiers();
    }

    public XClass<?> getSuperClass() {
        return Optional.ofNullable(javaClass.getSuperclass()).map(XClass::new).orElse(XNullClass.INSTANCE);
    }

    public Stream<XClass<?>> getInterfaces() {
        return Arrays.stream(Optional.ofNullable(javaClass.getInterfaces()).orElse(new Class[0])).map(XClass::new);
    }

    public Stream<XClass<?>> getAllInterfaces() {
        return Stream.concat(getInterfaces(), getSuperClass().getAllInterfaces()).distinct();
    }

    public XClassPackage getPackage() {
        return new XClassPackage(javaClass);
    }

    public Stream<XConstructor<T>> getConstructors() {
        return Arrays.stream(javaClass.getDeclaredConstructors())
            .map(c -> new XConstructor(c));
    }

    public Stream<XField> getFields() {
        return Arrays.stream(javaClass.getDeclaredFields()).map(XField::new);
    }

    public Stream<XField> getAllFields() {
        return Stream.concat(getFields(), getSuperClass().getAllFields());
    }

    public Stream<XMethod<?>> getMethods() {
        return Arrays.stream(javaClass.getDeclaredMethods()).map(XMethod::new);
    }

    public Stream<XMethod<?>> getAllMethods() {
        return Stream.concat(getMethods(), getSuperClass().getAllMethods());
    }

}

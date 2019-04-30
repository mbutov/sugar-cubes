package org.sugarcubes.reflection;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.Arrays.stream;

import org.sugarcubes.check.Args;
import static org.sugarcubes.reflection.XPredicates.withName;
import static org.sugarcubes.reflection.XPredicates.withNameAndParameterTypes;
import static org.sugarcubes.reflection.XPredicates.withParameterTypes;
import static org.sugarcubes.reflection.XReflectiveOperationException.withMessage;
import static org.sugarcubes.stream.ZeroOneCollectors.onlyElement;
import static org.sugarcubes.stream.ZeroOneCollectors.toOptional;

/**
 * Wrapper for {@link Class}.
 *
 * @author Maxim Butov
 */
public class XClass<C> extends XReflectionObjectImpl<Class<C>> implements XAnnotated<Class<C>>, XModifiers {

    private final Class<C> reflectionObject;

    XClass(Class<C> reflectionObject) {
        this.reflectionObject = Args.notNull(reflectionObject, "Class must not be null");
    }

    @Override
    public Class<C> getReflectionObject() {
        return reflectionObject;
    }

    public boolean isExactClass(Class<?> clazz) {
        return getReflectionObject().equals(clazz);
    }

    public boolean isAssignableFrom(Class<?> clazz) {
        return getReflectionObject().isAssignableFrom(clazz);
    }

    public boolean isSubclassOf(Class<?> clazz) {
        return clazz.isAssignableFrom(getReflectionObject());
    }

    @Override
    public int getModifiers() {
        return getReflectionObject().getModifiers();
    }

    @Override
    public String getName() {
        return getReflectionObject().getName();
    }

    public boolean isNull() {
        return false;
    }

    public XClass<?> getSuperclass() {
        return XClassCache.get(this, XClassUtils::getSuperclass);
    }

    public Stream<XClass<?>> getInheritance() {
        return XClassCache.get(this, XClassUtils::getInheritance).stream();
    }

    public Stream<XClass<?>> getDeclaredInterfaces() {
        return XClassCache.get(this, XClassUtils::getDeclaredInterfaces).stream();
    }

    public Stream<XClass<?>> getInterfaces() {
        return XClassCache.get(this, XClassUtils::getInterfaces).stream();
    }

    public Stream<XConstructor<C>> getConstructors() {
        return XClassCache.get(this, XClassUtils::getConstructors).stream();
    }

    public Stream<XField<?>> getDeclaredFields() {
        return XClassCache.get(this, XClassUtils::getDeclaredFields).stream();
    }

    public Stream<XField<?>> getFields() {
        return XClassCache.get(this, XClassUtils::getFields).stream();
    }

    public Stream<XMethod<?>> getDeclaredMethods() {
        return XClassCache.get(this, XClassUtils::getDeclaredMethods).stream();
    }

    public Stream<XMethod<?>> getMethods() {
        return XClassCache.get(this, XClassUtils::getMethods).stream();
    }

    public Optional<XConstructor<C>> findConstructor(Class... types) {
        return getConstructors().filter(withParameterTypes(types)).collect(toOptional());
    }

    public XConstructor<C> getConstructor(Class... types) {
        return findConstructor(types)
            .orElseThrow(withMessage(() -> String.format("Constructor %s(%s) not found", getName(), getParameterNames(types))));
    }

    public <X> Optional<XField<X>> findDeclaredField(String name) {
        return getDeclaredFields().filter(withName(name)).map(XField::<X>cast).collect(toOptional());
    }

    public <X> XField<X> getDeclaredField(String name) {
        return this.<X>findDeclaredField(name)
            .orElseThrow(withMessage("Field %s.%s not found", getName(), name));
    }

    public <X> Stream<XField<X>> findFields(String name) {
        return getFields().filter(withName(name)).map(XField::<X>cast);
    }

    public <X> XField<X> getField(String name) {
        return this.<X>findFields(name)
            .collect(onlyElement(
                withMessage("Two or more fields with name %s found in %s hierarchy", name, getName()),
                withMessage("No field %s found in %s hierarchy", name, getName())
            ));
    }

    public <X> Optional<XMethod<X>> findDeclaredMethod(String name, Class... types) {
        return getDeclaredMethods()
            .map(XMethod::<X>cast)
            .filter(withNameAndParameterTypes(name, types))
            .collect(toOptional());
    }

    public <X> XMethod<X> getDeclaredMethod(String name, Class... types) {
        return this.<X>findDeclaredMethod(name, types)
            .orElseThrow(withMessage(() ->
                String.format("Method %s.%s(%s) not found", getName(), name, getParameterNames(types))));
    }

    public <X> Optional<XMethod<X>> findMethod(String name, Class... types) {
        return getMethods()
            .map(XMethod::<X>cast)
            .filter(withNameAndParameterTypes(name, types))
            .findFirst();
    }

    public <X> XMethod<X> getMethod(String name, Class... types) {
        return this.<X>findMethod(name, types)
            .orElseThrow(withMessage(() -> String.format("Method %s.%s(%s) not found in class hierarchy", getName(), name, getParameterNames(types))));
    }

    private static String getParameterNames(Class[] types) {
        return stream(types).map(Class::getName).collect(Collectors.joining(","));
    }

}

package org.sugarcubes.reflection;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.Arrays.stream;

import org.sugarcubes.arg.Arg;
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
public class XClass<C> extends XReflectionObjectImpl<Class<C>> implements XAnnotated<Class<C>>, XModifiers, XTyped<C> {

    private final Class<C> reflectionObject;

    XClass(Class<C> reflectionObject) {
        this.reflectionObject = Arg.notNull(reflectionObject, "Class must not be null");
    }

    @Override
    public Class<C> getReflectionObject() {
        return reflectionObject;
    }

    @Override
    public Class<C> getType() {
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

    public boolean isNull() {
        return false;
    }

    private enum CacheKey {
        SUPER, INHERITANCE, DECLARED_INTERFACES, INTERFACES, CONSTRUCTORS, DECLARED_METHODS, METHODS, DECLARED_FIELDS, FIELDS,
    }

    private final Map<CacheKey, Object> cache = new XClassCache<>();

    private <X> X computeIfAbsent(CacheKey key, Function<XClass<C>, X> function) {
        return (X) cache.computeIfAbsent(key, k -> function.apply(this));
    }

    public XClass<?> getSuperclass() {
        return computeIfAbsent(CacheKey.SUPER, XClassUtils::getSuperclass);
    }

    public Stream<XClass<?>> getInheritance() {
        return computeIfAbsent(CacheKey.INHERITANCE, XClassUtils::getInheritance).stream();
    }

    public Stream<XClass<?>> getDeclaredInterfaces() {
        return computeIfAbsent(CacheKey.DECLARED_INTERFACES, XClassUtils::getDeclaredInterfaces).stream();
    }

    public Stream<XClass<?>> getInterfaces() {
        return computeIfAbsent(CacheKey.INTERFACES, XClassUtils::getInterfaces).stream();
    }

    public Stream<XConstructor<C>> getConstructors() {
        return computeIfAbsent(CacheKey.CONSTRUCTORS, XClassUtils::getConstructors).stream();
    }

    public Stream<XField<?>> getDeclaredFields() {
        return computeIfAbsent(CacheKey.DECLARED_FIELDS, XClassUtils::getDeclaredFields).stream();
    }

    public Stream<XField<?>> getFields() {
        return computeIfAbsent(CacheKey.FIELDS, XClassUtils::getFields).stream();
    }

    public Stream<XMethod<?>> getDeclaredMethods() {
        return computeIfAbsent(CacheKey.DECLARED_METHODS, XClassUtils::getDeclaredMethods).stream();
    }

    public Stream<XMethod<?>> getMethods() {
        return computeIfAbsent(CacheKey.METHODS, XClassUtils::getMethods).stream();
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

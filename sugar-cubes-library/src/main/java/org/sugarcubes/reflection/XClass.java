package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.Arrays.stream;

import org.sugarcubes.builder.collection.ListBuilder;
import org.sugarcubes.check.Checks;
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
        this.reflectionObject = Checks.arg().notNull(reflectionObject, "Class must not be null");
    }

    @Override
    public Class<C> getReflectionObject() {
        return reflectionObject;
    }

    public boolean isSameClass(Class<?> clazz) {
        return getReflectionObject().equals(clazz);
    }

    public boolean isSameClass(XClass<?> xClass) {
        return isSameClass(xClass.getReflectionObject());
    }

    public boolean isAssignableFrom(Class<?> clazz) {
        return getReflectionObject().isAssignableFrom(clazz);
    }

    public boolean isAssignableFrom(XClass<?> xClass) {
        return isAssignableFrom(xClass.getReflectionObject());
    }

    public boolean isSubclassOf(Class<?> clazz) {
        return clazz.isAssignableFrom(getReflectionObject());
    }

    public boolean isSubclassOf(XClass<?> xClass) {
        return isSubclassOf(xClass.getReflectionObject());
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
        return computeIfAbsent(this::getSuperclassInternal);
    }

    public Stream<XClass<?>> getInheritance() {
        return computeIfAbsent(this::getInheritanceInternal).stream();
    }

    public Stream<XClass<?>> getDeclaredInterfaces() {
        return computeIfAbsent(this::getDeclaredInterfacesInternal).stream();
    }

    public Stream<XClass<?>> getInterfaces() {
        return computeIfAbsent(this::getInterfacesInternal).stream();
    }

    public Stream<XConstructor<C>> getConstructors() {
        return computeIfAbsent(this::getConstructorsInternal).stream();
    }

    public Stream<XField<?>> getDeclaredFields() {
        return computeIfAbsent(this::getDeclaredFieldsInternal).stream();
    }

    public Stream<XField<?>> getFields() {
        return computeIfAbsent(this::getFieldsInternal).stream();
    }

    public Stream<XMethod<?>> getDeclaredMethods() {
        return computeIfAbsent(this::getDeclaredMethodsInternal).stream();
    }

    public Stream<XMethod<?>> getMethods() {
        return computeIfAbsent(this::getMethodsInternal).stream();
    }

    public Optional<XConstructor<C>> findConstructor(Class<?>... types) {
        return getConstructors().filter(withParameterTypes(types)).collect(toOptional());
    }

    public XConstructor<C> getConstructor(Class<?>... types) {
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

    public <X> Optional<XMethod<X>> findDeclaredMethod(String name, Class<?>... types) {
        return getDeclaredMethods()
            .map(XMethod::<X>cast)
            .filter(withNameAndParameterTypes(name, types))
            .collect(toOptional());
    }

    public <X> XMethod<X> getDeclaredMethod(String name, Class<?>... types) {
        return this.<X>findDeclaredMethod(name, types)
            .orElseThrow(withMessage(() ->
                String.format("Method %s.%s(%s) not found", getName(), name, getParameterNames(types))));
    }

    public <X> Optional<XMethod<X>> findMethod(String name, Class<?>... types) {
        return getMethods()
            .map(XMethod::<X>cast)
            .filter(withNameAndParameterTypes(name, types))
            .findFirst();
    }

    public <X> XMethod<X> getMethod(String name, Class<?>... types) {
        return this.<X>findMethod(name, types)
            .orElseThrow(withMessage(() -> String.format("Method %s.%s(%s) not found in class hierarchy", getName(), name, getParameterNames(types))));
    }

    private static String getParameterNames(Class<?>[] types) {
        return stream(types).map(Class::getName).collect(Collectors.joining(","));
    }

    private transient Map<Supplier<?>, Object> cache;

    private <X> Map<Supplier<X>, X> getCache() {
        if (this.cache == null) {
            this.cache = new IdentityHashMap<>();
        }
        return (Map) this.cache;
    }

    private <X> X computeIfAbsent(Supplier<X> method) {
        Map<Supplier<X>, X> cache = getCache();
        return cache.computeIfAbsent(method, Supplier::get);
    }

    private XClass<?> getSuperclassInternal() {
        return Optional.ofNullable(getReflectionObject().getSuperclass())
            .map(XReflection::of)
            .orElse((XClass) XNullClass.INSTANCE);
    }

    private List<XClass<?>> getInheritanceInternal() {
        return Stream.concat(Stream.of(this), getSuperclass().getInheritance()).collect(Collectors.toList());
    }

    private List<XClass<?>> getDeclaredInterfacesInternal() {
        return Stream.of(getReflectionObject().getInterfaces()).map(XReflection::of).collect(Collectors.toList());
    }

    private List<XClass<?>> getInterfacesInternal() {
        if (isInterface()) {
            return Collections.singletonList(this);
        }
        return Stream.concat(getDeclaredInterfaces(), getSuperclass().getDeclaredInterfaces())
            .distinct()
            .collect(Collectors.toList());
    }

    private List<XConstructor<C>> getConstructorsInternal() {
        Constructor<C>[] constructors = (Constructor[]) getReflectionObject().getDeclaredConstructors();
        return ListBuilder.<XConstructor<C>>arrayList()
            .addAll(stream(constructors).map(XReflection::of))
            .transform(Collections::unmodifiableList)
            .build();
    }

    private List<XField<?>> getDeclaredFieldsInternal() {
        return ListBuilder.<XField<?>>arrayList()
            .addAll(stream(getReflectionObject().getDeclaredFields()).map(XReflection::of))
            .transform(Collections::unmodifiableList)
            .build();
    }

    private List<XField<?>> getFieldsInternal() {
        return ListBuilder.<XField<?>>arrayList()
            .addAll(getDeclaredFields())
            .addAll(getSuperclass().getFields())
            .transform(Collections::unmodifiableList)
            .build();
    }

    private List<XMethod<?>> getDeclaredMethodsInternal() {
        return ListBuilder.<XMethod<?>>arrayList()
            .addAll(stream(getReflectionObject().getDeclaredMethods()).map(XReflection::of))
            .transform(Collections::unmodifiableList)
            .build();
    }

    private List<XMethod<?>> getMethodsInternal() {
        return ListBuilder.<XMethod<?>>arrayList()
            .addAll(getDeclaredMethods())
            .addAll(getSuperclass().getMethods())
            .transform(Collections::unmodifiableList)
            .build();
    }

}

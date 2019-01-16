package org.sugarcubes.reflection;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.Arrays.stream;

import org.sugarcubes.builder.ListBuilder;
import org.sugarcubes.builder.SetBuilder;
import org.sugarcubes.validation.Arg;
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

    private transient XClass<?> xSuper;
    private transient List<XClass<?>> inheritance;
    private transient List<XClass<?>> xDeclaredInterfaces;
    private transient List<XClass<?>> xInterfaces;
    private transient List<XConstructor<C>> xConstructors;
    private transient List<XField<?>> xDeclaredFields;

    public XClass<?> getSuperclass() {
        if (xSuper == null) {
            Class<?> superclass = getReflectionObject().getSuperclass();
            xSuper = superclass != null ? XReflection.of(superclass) : XNullClass.INSTANCE;
        }
        return xSuper;
    }

    public boolean isNull() {
        return false;
    }

    public Stream<XClass<?>> getInheritance() {
        if (inheritance == null) {
            inheritance = ListBuilder.<XClass<?>>arrayList()
                .add(this)
                .addAll(getSuperclass().getInheritance())
                .transform(Collections::unmodifiableList)
                .build();
        }
        return inheritance.stream();
    }

    public Stream<XClass<?>> getDeclaredInterfaces() {
        if (xDeclaredInterfaces == null) {
            xDeclaredInterfaces = SetBuilder.<XClass<?>>linkedHashSet()
                .addAll(stream(getReflectionObject().getInterfaces()).map(XReflection::of))
                .transform(ArrayList::new)
                .transform(Collections::unmodifiableList)
                .build();
        }
        return xDeclaredInterfaces.stream();
    }

    public Stream<XClass<?>> getInterfaces() {
        if (xInterfaces == null) {
            Stream<XClass<?>> stream = Stream.concat(getDeclaredInterfaces(), getSuperclass().getInterfaces()).distinct();
            xInterfaces = SetBuilder.<XClass<?>>linkedHashSet()
                .addAll(getDeclaredInterfaces())
                .addAll(getSuperclass().getInterfaces())
                .transform(ArrayList::new)
                .transform(Collections::unmodifiableList)
                .build();
        }
        return xInterfaces.stream();
    }

    public Stream<XConstructor<C>> getConstructors() {
        if (xConstructors == null) {
            xConstructors = ListBuilder.<XConstructor<C>>arrayList()
                .addAll(stream((Constructor<C>[]) getReflectionObject().getDeclaredConstructors()).map(XReflection::of))
                .transform(Collections::unmodifiableList)
                .build();
        }
        return xConstructors.stream();
    }

    public Optional<XConstructor<C>> findConstructor(Class... types) {
        return getConstructors().filter(withParameterTypes(types)).collect(toOptional());
    }

    public XConstructor<C> getConstructor(Class... types) {
        return findConstructor(types)
            .orElseThrow(withMessage(() -> String.format("Constructor %s(%s) not found", getName(), getParameterNames(types))));
    }

    public Stream<XField<?>> getDeclaredFields() {
        if (xDeclaredFields == null) {
            xDeclaredFields = ListBuilder.<XField<?>>arrayList()
                .addAll(stream(getReflectionObject().getDeclaredFields()).map(XReflection::of))
                .transform(Collections::unmodifiableList)
                .build();
        }
        return xDeclaredFields.stream();
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
